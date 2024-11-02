package org.example.infrastructure.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.core.dtos.habit_dtos.CreateHabitDto;
import org.example.core.dtos.habit_dtos.UpdateHabitDto;
import org.example.core.models.Habit;
import org.example.core.models.HabitFrequency;
import org.example.core.services.HabitService;
import org.example.infrastructure.exception_handlers.GlobalExceptionHandler;
import org.example.infrastructure.exception_handlers.HabitExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class HabitControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @Mock
    private HabitService habitService;

    @InjectMocks
    private HabitController habitController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(habitController)
                .setControllerAdvice(new HabitExceptionHandler(), new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("Should return a list of habits when the user is authorized")
    void getUserHabits_shouldReturnListOfHabits_whenUserIsAuthorized() throws Exception {
        List<Habit> habits = List.of(
                new Habit(1, 1, "name1", "description1", HabitFrequency.DAILY, LocalDate.of(2024, 11, 2)),
                new Habit(2, 1, "name2", "description2", HabitFrequency.DAILY, LocalDate.of(2024, 11, 2))
        );
        String expectedJson = """
                [
                  {
                    "id": 1,
                    "userId": 1,
                    "name": "name1",
                    "description": "description1",
                    "frequency": "DAILY",
                    "dayOfCreation": [
                      2024,
                      11,
                      2
                    ]
                  },
                  {
                    "id": 2,
                    "userId": 1,
                    "name": "name2",
                    "description": "description2",
                    "frequency": "DAILY",
                    "dayOfCreation": [
                      2024,
                      11,
                      2
                    ]
                  }
                ]
                """;
        when(habitService.getUserHabits(1)).thenReturn(habits);

        mockMvc.perform(get("/api/v2/habits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 1")
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(habitService).getUserHabits(1);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Bearer", "Bearer lalala", "Bearer ", ""})
    @DisplayName("Should return unauthorized status when the token is invalid")
    void getUserHabits_shouldReturnUnauthorized_whenTokenIsInvalid(String token) throws Exception {
        mockMvc.perform(get("/api/v2/habits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("User is not authorized or token is invalid"));
    }

    @Test
    @DisplayName("Should create a habit when the user is authorized")
    void createUserHabit_shouldCreateHabit_whenUserIsAuthorized() throws Exception {
        CreateHabitDto dto = new CreateHabitDto(1, "name1", "description1", "DAILY");

        mockMvc.perform(post("/api/v2/habits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 1")
                        .content(objectMapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk());

        verify(habitService).createHabit(dto);
    }

    @Test
    @DisplayName("Should return a specific habit when the user is authorized")
    void getUserHabit_shouldReturnHabit_whenUserIsAuthorized() throws Exception {
        Habit habit = new Habit(1, 1, "name1", "description1", HabitFrequency.DAILY, LocalDate.of(2024, 11, 2));
        String expectedJson = """
                {
                  "id": 1,
                  "userId": 1,
                  "name": "name1",
                  "description": "description1",
                  "frequency": "DAILY",
                  "dayOfCreation": [
                    2024,
                    11,
                    2
                  ]
                }
                """;
        when(habitService.getUserHabit(1, 1)).thenReturn(habit);

        mockMvc.perform(get("/api/v2/habits/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 1")
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(habitService).getUserHabit(1, 1);
    }

    @Test
    @DisplayName("Should update a habit when the user is authorized")
    void updateUserHabit_shouldUpdateHabit_whenUserIsAuthorized() throws Exception {
        UpdateHabitDto habit = new UpdateHabitDto("name1", "description1", "DAILY");

        mockMvc.perform(put("/api/v2/habits/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 1")
                        .content(objectMapper.writeValueAsString(habit))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk());

        verify(habitService).updateUserHabit(1, 1, habit);
    }

    @Test
    @DisplayName("Should return the deadline of a habit when the user is authorized")
    void getHabitDeadlineDay_shouldReturnDeadline_whenUserIsAuthorized() throws Exception {
        when(habitService.getHabitDeadlineDay(1, 1)).thenReturn(LocalDate.of(2024, 11, 2));
        mockMvc.perform(get("/api/v2/habits/1/deadline")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 1")
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"deadline\":[2024, 11, 2]}"));

        verify(habitService).getHabitDeadlineDay(1, 1);
    }

    @Test
    @DisplayName("Should return the completion status of a habit when the user is authorized")
    void getHabitCompletionStatus_shouldReturnCompletionStatus_whenUserIsAuthorized() throws Exception {
        when(habitService.isCompleteUserHabit(1, 1)).thenReturn(true);
        mockMvc.perform(get("/api/v2/habits/1/completion-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 1")
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"completion_status\":true}"));

        verify(habitService).isCompleteUserHabit(1, 1);
    }

    @Test
    @DisplayName("Should return statistics of all user habits when the user is authorized")
    void getStatisticsOfAllUserHabits_shouldReturnStatistics_whenUserIsAuthorized() throws Exception {
        when(habitService.getStatisticsOfAllUserHabits(1)).thenReturn(List.of(
                        Map.of(
                                "habit_id", 1,
                                "completion_percent", 100,
                                "track_count", 1,
                                "current_streak", 1,
                                "completion_status", true

                        ),
                        Map.of(
                                "habit_id", 2,
                                "completion_percent", 0,
                                "track_count", 0,
                                "current_streak", 0,
                                "completion_status", false

                        )
                )
        );
        String expectedJson = """
                [
                  {
                    "completion_status": true,
                    "completion_percent": 100,
                    "track_count": 1,
                    "habit_id": 1,
                    "current_streak": 1
                  },
                  {
                    "completion_status": false,
                    "completion_percent": 0,
                    "track_count": 0,
                    "habit_id": 2,
                    "current_streak": 0
                  }
                ]
                """;
        mockMvc.perform(get("/api/v2/habits/statistics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 1")
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(habitService).getStatisticsOfAllUserHabits(1);
    }
}