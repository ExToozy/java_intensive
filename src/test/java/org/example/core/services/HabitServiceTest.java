package org.example.core.services;

import org.example.core.models.Habit;
import org.example.core.models.HabitFrequency;
import org.example.core.repositories.habit_repository.dtos.CreateHabitDto;
import org.example.core.repositories.habit_repository.dtos.UpdateHabitDto;
import org.example.infastructure.data.models.HabitEntity;
import org.example.infastructure.data.models.HabitTrackEntity;
import org.example.infastructure.data.repositories.in_memory_repositories.InMemoryHabitRepository;
import org.example.infastructure.data.repositories.in_memory_repositories.InMemoryHabitTrackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class HabitServiceTest {
    HabitService habitService;
    HabitTrackService habitTrackService;

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        var userId = UUID.fromString("00000000-0000-0000-0000-000000000000");
        var habits = new ArrayList<>(Arrays.asList(
                new HabitEntity(
                        UUID.fromString("00000000-0000-0000-0000-000000000000"),
                        userId,
                        "testUser1HabitName",
                        "testUser1HabitDescription",
                        HabitFrequency.DAILY,
                        LocalDate.now()
                ),
                new HabitEntity(
                        UUID.fromString("00000000-0000-0000-0000-000000000001"),
                        userId,
                        "testUser1HabitName",
                        "testUser1HabitDescription",
                        HabitFrequency.DAILY,
                        LocalDate.now()
                ),
                new HabitEntity(
                        UUID.fromString("00000000-0000-0000-0000-000000000002"),
                        userId,
                        "testUser1HabitName",
                        "testUser1HabitDescription",
                        HabitFrequency.WEEKLY,
                        LocalDate.now().minusDays(Period.ofWeeks(3).getDays())
                ),
                new HabitEntity(
                        UUID.fromString("00000000-0000-0000-0000-000000000003"),
                        UUID.fromString("00000000-0000-0000-0000-000000000001"),
                        "testUser2HabitName",
                        "testUser2HabitDescription",
                        HabitFrequency.DAILY,
                        LocalDate.now().minusDays(1)
                )
        )
        );

        var tracks = new ArrayList<>(Arrays.asList(
                new HabitTrackEntity(
                        UUID.fromString("00000000-0000-0000-0000-000000000000"),
                        UUID.fromString("00000000-0000-0000-0000-000000000000"),
                        LocalDate.now()
                ),
                new HabitTrackEntity(
                        UUID.fromString("00000000-0000-0000-0000-000000000001"),
                        UUID.fromString("00000000-0000-0000-0000-000000000002"),
                        LocalDate.now().minusDays(Period.ofWeeks(0).getDays())
                ),
                new HabitTrackEntity(
                        UUID.fromString("00000000-0000-0000-0000-000000000002"),
                        UUID.fromString("00000000-0000-0000-0000-000000000002"),
                        LocalDate.now().minusDays(Period.ofWeeks(1).getDays())
                ),
                new HabitTrackEntity(
                        UUID.fromString("00000000-0000-0000-0000-000000000003"),
                        UUID.fromString("00000000-0000-0000-0000-000000000002"),
                        LocalDate.now().minusDays(Period.ofWeeks(2).getDays())
                ),
                new HabitTrackEntity(
                        UUID.fromString("00000000-0000-0000-0000-000000000004"),
                        UUID.fromString("00000000-0000-0000-0000-000000000010"),
                        LocalDate.now().minusDays(1)
                )
        )
        );
        InMemoryHabitRepository habitRepository = new InMemoryHabitRepository();
        InMemoryHabitTrackRepository habitTrackRepository = new InMemoryHabitTrackRepository();
        Field habitsField = InMemoryHabitRepository.class.getDeclaredField("habits");
        Field tracksField = InMemoryHabitTrackRepository.class.getDeclaredField("tracks");
        habitsField.setAccessible(true);
        habitsField.set(habitRepository, habits);
        tracksField.setAccessible(true);
        tracksField.set(habitTrackRepository, tracks);
        habitTrackService = new HabitTrackService(habitTrackRepository);
        habitService = new HabitService(habitRepository, habitTrackService);
    }

    @DisplayName("Check that getUserHabits return user habits")
    @Test
    void getUserHabits_shouldReturnHabits_whenUserHasHabits() {
        var habitsUser1 = habitService.getUserHabits(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        var habitsUser2 = habitService.getUserHabits(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        assertThat(habitsUser1).isNotNull().isNotEmpty().hasSize(3);
        assertThat(habitsUser2).isNotNull().isNotEmpty().hasSize(1);
    }

    @DisplayName("Check create habit")
    @Test
    void createHabit_shouldAddHabitInMemory_whenAllIsCorrect() {
        habitService.createHabit(
                new CreateHabitDto(
                        UUID.fromString("00000000-0000-0000-0000-000000000002"),
                        "testName",
                        "testDescription",
                        HabitFrequency.DAILY
                )
        );

        var userHabits = habitService.getUserHabits(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        assertThat(userHabits)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .allMatch(habit -> habit.getId() != null &&
                        habit.getName().equals("testName") &&
                        habit.getDescription().equals("testDescription") &&
                        habit.getFrequency().equals(HabitFrequency.DAILY) &&
                        habit.getDayOfCreation().equals(LocalDate.now())
                );
    }

    @DisplayName("Check that getUserHabitsByCompleteStatus return only completed habits")
    @Test
    void getUserHabitsByCompleteStatus_shouldReturnOnlyCompletedHabits_whenWeRequestTrueStatus() {
        var habits = habitService.getUserHabitsByCompleteStatus(UUID.fromString("00000000-0000-0000-0000-000000000000"), true);
        assertThat(habits).isNotNull().isNotEmpty().allMatch(habit -> habitService.isCompleteHabit(habit));
    }

    @DisplayName("Check that getUserHabitsByCompleteStatus return only not completed habits")
    @Test
    void getUserHabitsByCompleteStatus_shouldReturnOnlyNotCompletedHabits_whenWeRequestFalseStatus() {
        var habits = habitService.getUserHabitsByCompleteStatus(UUID.fromString("00000000-0000-0000-0000-000000000000"), false);
        assertThat(habits).isNotNull().isNotEmpty().allMatch(habit -> !habitService.isCompleteHabit(habit));
    }

    @DisplayName("Check isCompleteHabit should return true if habit is really complete")
    @Test
    void isCompleteHabit_shouldReturnTrue_WhenHabitIsComplete() {
        Habit habit = new Habit(
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                "testUser1HabitName",
                "testUser1HabitDescription",
                HabitFrequency.DAILY,
                LocalDate.now().minusDays(1)
        );
        assertThat(habitService.isCompleteHabit(habit)).isTrue();
    }

    @DisplayName("Check execution count by period for the habit is correct")
    @Test
    void getHabitExecutionCountByPeriod_shouldReturnNumberOfExecution_whenHabitHaveExecutionInRequestPeriod() {
        var habit = new Habit(
                UUID.fromString("00000000-0000-0000-0000-000000000002"),
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                "testUser1HabitName",
                "testUser1HabitDescription",
                HabitFrequency.WEEKLY,
                LocalDate.now().minusDays(Period.ofWeeks(3).getDays())
        );
        assertThat(habitService.getHabitExecutionCountByPeriod(habit, Period.ofWeeks(1))).isEqualTo(1);
        assertThat(habitService.getHabitExecutionCountByPeriod(habit, Period.ofWeeks(2))).isEqualTo(2);
        assertThat(habitService.getHabitExecutionCountByPeriod(habit, Period.ofWeeks(3))).isEqualTo(3);
        assertThat(habitService.getHabitExecutionCountByPeriod(habit, Period.ofWeeks(4))).isEqualTo(3);
    }

    @DisplayName("Check statistics for the habit is correct")
    @Test
    void getHabitStatistics_shouldReturnCorrectHabitsStatistics() {
        var statistics = habitService.getHabitStatistics(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        for (var habitStat : statistics.entrySet()) {
            if (habitStat.getKey().getId().equals(UUID.fromString("00000000-0000-0000-0000-000000000001"))) {
                assertThat(habitStat.getValue().get("completion_percent")).isZero();
                assertThat(habitStat.getValue().get("track_count")).isZero();
                assertThat(habitStat.getValue().get("current_streak")).isZero();
            } else if (habitStat.getKey().getId().equals(UUID.fromString("00000000-0000-0000-0000-000000000002"))) {
                assertThat(habitStat.getValue().get("completion_percent")).isEqualTo(75);
                assertThat(habitStat.getValue().get("track_count")).isEqualTo(3);
                assertThat(habitStat.getValue().get("current_streak")).isEqualTo(3);
            } else {
                assertThat(habitStat.getValue().get("completion_percent")).isEqualTo(100);
                assertThat(habitStat.getValue().get("track_count")).isEqualTo(1);
                assertThat(habitStat.getValue().get("current_streak")).isEqualTo(1);
            }
        }

    }

    @DisplayName("Check streak for the habit is correct")
    @Test
    void getHabitStreak_shouldReturnHabitStreak_whenHabitHasStreak() {
        var habit = new Habit(
                UUID.fromString("00000000-0000-0000-0000-000000000002"),
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                "testUser1HabitName",
                "testUser1HabitDescription",
                HabitFrequency.WEEKLY,
                LocalDate.now().minusDays(Period.ofWeeks(3).getDays())
        );
        assertThat(habitService.getHabitStreak(habit)).isEqualTo(3);
    }

    @DisplayName("Check remove habits and their tracks")
    @Test
    void removeHabitAndTracks_shouldRemoveHabitAndTheirTracksInMemory() {
        habitService.removeHabitAndTracks(UUID.fromString("00000000-0000-0000-0000-000000000003"));
        var userHabits = habitService.getUserHabits(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        var habitTracks = habitTrackService.getHabitTracks(UUID.fromString("00000000-0000-0000-0000-000000000003"));

        assertThat(userHabits).isNotNull().isEmpty();
        assertThat(habitTracks).isNotNull().isEmpty();
    }

    @DisplayName("Check update habit")
    @Test
    void updateHabit_shouldUpdateHabit() {
        habitService.updateHabit(
                new UpdateHabitDto(
                        UUID.fromString("00000000-0000-0000-0000-000000000003"),
                        "newHabitName",
                        "newHabitDescription",
                        HabitFrequency.WEEKLY
                )
        );
        var updatedHabit = habitService
                .getUserHabits(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .get(0);

        assertThat(updatedHabit.getId()).isEqualTo(UUID.fromString("00000000-0000-0000-0000-000000000003"));
        assertThat(updatedHabit.getName()).isEqualTo("newHabitName");
        assertThat(updatedHabit.getDescription()).isEqualTo("newHabitDescription");
        assertThat(updatedHabit.getFrequency()).isEqualTo(HabitFrequency.WEEKLY);
        assertThat(updatedHabit.getDayOfCreation()).isEqualTo(LocalDate.now().minusDays(1));
    }

    @DisplayName("Check deadline day for the habit is correct")
    @Test
    void getHabitDeadlineDay_shouldGetCorrectDeadlineDay() {
        var habit = new Habit(
                UUID.fromString("00000000-0000-0000-0000-000000000002"),
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                "testUser1HabitName",
                "testUser1HabitDescription",
                HabitFrequency.WEEKLY,
                LocalDate.now().minusDays(Period.ofWeeks(3).getDays())
        );
        assertThat(habitService.getHabitDeadlineDay(habit)).isEqualTo(LocalDate.now().plusDays(7));
    }
}
