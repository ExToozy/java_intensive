package org.example.core.services;

import org.assertj.core.api.SoftAssertions;
import org.example.core.dtos.habit_dtos.CreateHabitDto;
import org.example.core.dtos.habit_dtos.UpdateHabitDto;
import org.example.core.models.HabitFrequency;
import org.example.core.models.User;
import org.example.exceptions.HabitNotFoundException;
import org.example.exceptions.InvalidTokenException;
import org.example.infrastructure.util.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.Period;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(value = "classpath:application.yml")
@Sql(value = {"classpath:test_sql_scripts/remove-all-data.sql", "classpath:test_sql_scripts/insert-test-data.sql"})
class HabitServiceTest {

    @Autowired
    HabitService habitService;

    @Autowired
    HabitTrackService habitTrackService;

    @Autowired
    JwtProvider jwtProvider;
    private String testTokenForUser1;
    private String testTokenForUser2;

    @BeforeEach
    void setUp() {
        testTokenForUser1 = "Bearer " + jwtProvider.generateAccessToken(new User(1, "ex@mail.ru", "password", false));
        testTokenForUser2 = "Bearer " + jwtProvider.generateAccessToken(new User(2, "admin", "password", true));
    }


    @DisplayName("Check that getUserHabits return user habits")
    @Test
    void getUserHabits_shouldReturnHabits_whenUserHasHabits() {
        var habitsUser1 = habitService.getUserHabits(1);

        assertThat(habitsUser1).isNotNull().isNotEmpty().hasSize(3);
    }

    @DisplayName("Check create habit")
    @Test
    void createHabit_shouldAddHabitInDb_whenAllIsCorrect() throws InvalidTokenException {
        habitService.createUserHabit(
                testTokenForUser2, new CreateHabitDto(
                        2,
                        "testName",
                        "testDescription",
                        "DAILY"
                )
        );

        var userHabits = habitService.getUserHabits(2);
        assertThat(userHabits)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .allMatch(habit ->
                        habit.getName().equals("testName") &&
                                habit.getDescription().equals("testDescription") &&
                                habit.getFrequency().equals(HabitFrequency.DAILY) &&
                                habit.getDayOfCreation().equals(LocalDate.now())
                );
    }

    @DisplayName("Check that getUserHabitsByCompleteStatus return only completed habits")
    @Test
    void getUserHabitsByCompleteStatus_shouldReturnOnlyCompletedHabits_whenWeRequestTrueStatus() {
        var habits = habitService.getUserHabitsByCompleteStatus(1, true);
        assertThat(habits).isNotNull().isNotEmpty().allMatch(habit -> {
            try {
                return habitService.isCompleteUserHabit(1, habit.getId());
            } catch (HabitNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @DisplayName("Check that getUserHabitsByCompleteStatus return only not completed habits")
    @Test
    void getUserHabitsByCompleteStatus_shouldReturnOnlyNotCompletedHabits_whenWeRequestFalseStatus() {
        var habits = habitService.getUserHabitsByCompleteStatus(1, false);
        assertThat(habits).isNotNull().isNotEmpty().allMatch(habit -> {
            try {
                return !habitService.isCompleteUserHabit(1, habit.getId());
            } catch (HabitNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @DisplayName("Check isCompleteHabit should return true if habit is really complete")
    @Test
    void isCompleteHabit_shouldReturnTrue_WhenHabitIsComplete() throws HabitNotFoundException {
        assertThat(habitService.isCompleteUserHabit(1, 1)).isTrue();
    }

    @DisplayName("Check statistics for the habit is correct")
    @Test
    void getHabitStatistics_shouldReturnCorrectHabitsStatistics() throws HabitNotFoundException {
        var statistics = habitService.getStatisticsOfOneUserHabit(1, 1);

        Map<String, Object> expectedStats = Map.of(
                "habit_id", 1,
                "completion_percent", 100,
                "track_count", 1,
                "current_streak", 1,
                "completion_status", true

        );

        assertThat(statistics).isNotNull().isEqualTo(expectedStats);

    }

    @DisplayName("Check remove habits and their tracks")
    @Test
    void removeHabitAndTracks_shouldRemoveHabitAndTheirTracksInDb() throws HabitNotFoundException, InvalidTokenException {
        habitService.removeUserHabit(1, 3);
        var userHabits = habitService.getUserHabits(1);


        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(userHabits).isNotNull().hasSize(2);
        softly.assertThatThrownBy(() -> habitTrackService.getHabitTracks(testTokenForUser1, 3))
                .isInstanceOf(HabitNotFoundException.class);
        softly.assertAll();
    }

    @DisplayName("Check update habit")
    @Test
    void updateHabit_shouldUpdateHabit() throws HabitNotFoundException {
        habitService.updateUserHabit(1, 3,
                new UpdateHabitDto(
                        "newHabitName",
                        "newHabitDescription",
                        "WEEKLY"
                )
        );
        var updatedHabit = habitService
                .getUserHabits(1)
                .stream().filter(habit -> habit.getId() == 3)
                .findFirst()
                .orElse(null);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(updatedHabit).isNotNull();
        softly.assertThat(updatedHabit.getId()).isEqualTo(3);
        softly.assertThat(updatedHabit.getName()).isEqualTo("newHabitName");
        softly.assertThat(updatedHabit.getDescription()).isEqualTo("newHabitDescription");
        softly.assertThat(updatedHabit.getFrequency()).isEqualTo(HabitFrequency.WEEKLY);
        softly.assertThat(updatedHabit.getDayOfCreation()).isEqualTo(LocalDate.now().minusDays(Period.ofWeeks(3).getDays()));
        softly.assertAll();
    }

    @DisplayName("Check deadline day for the habit is correct")
    @Test
    void getHabitDeadlineDay_shouldGetCorrectDeadlineDay() throws HabitNotFoundException {
        assertThat(habitService.getHabitDeadlineDay(1, 3)).isEqualTo(LocalDate.now().plusDays(7));
    }
}
