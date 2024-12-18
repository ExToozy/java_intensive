package org.example.infrastructure.controllers;

import org.example.core.models.HabitTrack;
import org.example.core.models.User;
import org.example.core.services.HabitTrackService;
import org.example.exceptions.HabitNotFoundException;
import org.example.exceptions.HabitTrackNotFoundException;
import org.example.infrastructure.exception_handlers.HabitExceptionHandler;
import org.example.infrastructure.exception_handlers.HabitTrackExceptionHandler;
import org.example.infrastructure.util.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class HabitTrackControllerTest {
    private String testToken;
    private MockMvc mockMvc;
    @Mock
    private HabitTrackService habitTrackService;
    @InjectMocks
    private HabitTrackController habitTrackController;

    @BeforeEach
    void setUp() {
        JwtProvider jwtProvider = new JwtProvider("a2V5a2V5a2V5a2V5a2V5a2V5a2V5a2V5a2V5a2V5a2V5a2V5a2V5a2V5");
        testToken = "Bearer " + jwtProvider.generateAccessToken(new User(1, "ex@mail.ru", "password", false));
        mockMvc = MockMvcBuilders.standaloneSetup(habitTrackController)
                .setControllerAdvice(new HabitExceptionHandler(), new HabitTrackExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("Should return habit tracks for a habit when the user is authorized")
    void getHabitTracks_whenUserIsAuthorized_thenReturnHabitTracks() throws Exception {
        List<HabitTrack> tracks = List.of(
                new HabitTrack(1, 1, LocalDate.of(2024, 11, 2)),
                new HabitTrack(2, 1, LocalDate.of(2024, 11, 1))
        );
        String expectedJson = """
                [
                  {
                    "id": 1,
                    "habitId": 1,
                    "completeDate": [
                      2024,
                      11,
                      2
                    ]
                  },
                  {
                    "id": 2,
                    "habitId": 1,
                    "completeDate": [
                      2024,
                      11,
                      1
                    ]
                  }
                ]
                """;

        when(habitTrackService.getHabitTracks(testToken, 1)).thenReturn(tracks);

        mockMvc.perform(get("/api/v1/tracks/by-habit-id/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", testToken)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(habitTrackService).getHabitTracks(testToken, 1);
    }

    @Test
    @DisplayName("Should remove all habit tracks for a habit when the user is authorized")
    void removeHabitTracks_whenUserIsAuthorized_thenRemoveHabitTracks() throws Exception {

        mockMvc.perform(delete("/api/v1/tracks/by-habit-id/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", testToken)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("Should remove a habit track when the user is authorized")
    void removeTrack_whenUserIsAuthorized_thenRemoveHabitTrack() throws Exception {

        mockMvc.perform(delete("/api/v1/tracks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", testToken)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("Should mark a habit as complete when the user is authorized")
    void completeHabit_whenUserIsAuthorized_thenCompleteHabit() throws Exception {

        mockMvc.perform(post("/api/v1/tracks/complete-habit/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", testToken)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Should return not found when habit not found while fetching habit tracks")
    void getHabitTracks_whenUserIsNotUserHabit_thenReturnNotFound() throws Exception {
        doThrow(HabitNotFoundException.class).when(habitTrackService).getHabitTracks(testToken, 1);

        mockMvc.perform(get("/api/v1/tracks/by-habit-id/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", testToken)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Habit not found"));

        verify(habitTrackService).getHabitTracks(testToken, 1);
    }

    @Test
    @DisplayName("Should return not found when habit not found while removing habit tracks")
    void removeHabitTracks_whenUserIsNotAuthorized_thenReturnNotFound() throws Exception {
        doThrow(HabitNotFoundException.class).when(habitTrackService).removeUserHabitTracks(testToken, 1);

        mockMvc.perform(delete("/api/v1/tracks/by-habit-id/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", testToken)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Habit not found"));

        verify(habitTrackService).removeUserHabitTracks(testToken, 1);
    }

    @Test
    @DisplayName("Should return bad request when habit track not found while removing track")
    void removeTrack_whenUserIsNotUserHabitTrack_thenReturnNotFound() throws Exception {
        doThrow(HabitTrackNotFoundException.class).when(habitTrackService).remove(testToken, 1);

        mockMvc.perform(delete("/api/v1/tracks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", testToken)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Habit track not found"));

        verify(habitTrackService).remove(testToken, 1);
    }

    @Test
    @DisplayName("Should return not found when habit not found while completing habit")
    void completeHabit_whenUserIsNotUserHabit_thenReturnNotFound() throws Exception {
        doThrow(HabitNotFoundException.class).when(habitTrackService).completeHabit(testToken, 1);

        mockMvc.perform(post("/api/v1/tracks/complete-habit/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", testToken)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Habit not found"));

        verify(habitTrackService).completeHabit(testToken, 1);
    }
}
