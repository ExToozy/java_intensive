package org.example.infrastructure.controllers;

import org.example.core.models.HabitTrack;
import org.example.core.services.HabitService;
import org.example.core.services.HabitTrackService;
import org.example.infrastructure.exception_handlers.HabitExceptionHandler;
import org.example.infrastructure.exception_handlers.HabitTrackExceptionHandler;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class HabitTrackControllerTest {
    private MockMvc mockMvc;

    @Mock
    private HabitService habitService;

    @Mock
    private HabitTrackService habitTrackService;

    @InjectMocks
    private HabitTrackController habitTrackController;

    @BeforeEach
    void setUp() {
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

        when(habitService.isUserHabitOrUserIsAdmin(1, 1)).thenReturn(true);
        when(habitTrackService.getHabitTracks(1)).thenReturn(tracks);

        mockMvc.perform(get("/api/v2/tracks/by-habit-id/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 1")
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(habitService).isUserHabitOrUserIsAdmin(1, 1);
        verify(habitTrackService).getHabitTracks(1);
    }

    @Test
    @DisplayName("Should remove all habit tracks for a habit when the user is authorized")
    void removeHabitTracks_whenUserIsAuthorized_thenRemoveHabitTracks() throws Exception {
        when(habitService.isUserHabitOrUserIsAdmin(1, 1)).thenReturn(true);

        mockMvc.perform(delete("/api/v2/tracks/by-habit-id/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 1")
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk());

        verify(habitService).isUserHabitOrUserIsAdmin(1, 1);
    }

    @Test
    @DisplayName("Should remove a habit track when the user is authorized")
    void removeTrack_whenUserIsAuthorized_thenRemoveHabitTrack() throws Exception {
        when(habitService.isUserHabitTrackOrUserIsAdmin(1, 1)).thenReturn(true);

        mockMvc.perform(delete("/api/v2/tracks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 1")
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk());

        verify(habitService).isUserHabitTrackOrUserIsAdmin(1, 1);
    }

    @Test
    @DisplayName("Should mark a habit as complete when the user is authorized")
    void completeHabit_whenUserIsAuthorized_thenCompleteHabit() throws Exception {
        when(habitService.isUserHabitOrUserIsAdmin(1, 1)).thenReturn(true);

        mockMvc.perform(post("/api/v2/tracks/complete-habit/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 1")
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk());

        verify(habitService).isUserHabitOrUserIsAdmin(1, 1);
    }

    @Test
    @DisplayName("Should return bad request when habit not found while fetching habit tracks")
    void getHabitTracks_whenUserIsNotAuthorized_thenReturnBadRequest() throws Exception {
        when(habitService.isUserHabitOrUserIsAdmin(1, 1)).thenReturn(false);

        mockMvc.perform(get("/api/v2/tracks/by-habit-id/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 1")
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Habit not found"));

        verify(habitService).isUserHabitOrUserIsAdmin(1, 1);
    }

    @Test
    @DisplayName("Should return bad request when habit not found while removing habit tracks")
    void removeHabitTracks_whenUserIsNotAuthorized_thenReturnBadRequest() throws Exception {
        when(habitService.isUserHabitOrUserIsAdmin(1, 1)).thenReturn(false);

        mockMvc.perform(delete("/api/v2/tracks/by-habit-id/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 1")
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Habit not found"));

        verify(habitService).isUserHabitOrUserIsAdmin(1, 1);
    }

    @Test
    @DisplayName("Should return bad request when habit track not found while removing track")
    void removeTrack_whenUserIsNotAuthorized_thenReturnBadRequest() throws Exception {
        when(habitService.isUserHabitTrackOrUserIsAdmin(1, 1)).thenReturn(false);

        mockMvc.perform(delete("/api/v2/tracks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 1")
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Habit track not found"));

        verify(habitService).isUserHabitTrackOrUserIsAdmin(1, 1);
    }

    @Test
    @DisplayName("Should return bad request when habit not found while completing habit")
    void completeHabit_whenUserIsNotAuthorized_thenReturnBadRequest() throws Exception {
        when(habitService.isUserHabitOrUserIsAdmin(1, 1)).thenReturn(false);

        mockMvc.perform(post("/api/v2/tracks/complete-habit/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 1")
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Habit not found"));

        verify(habitService).isUserHabitOrUserIsAdmin(1, 1);
    }
}
