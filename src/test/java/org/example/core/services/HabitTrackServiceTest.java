package org.example.core.services;

import org.example.core.dtos.habit_track_dtos.CreateHabitTrackDto;
import org.example.core.models.Habit;
import org.example.core.models.HabitFrequency;
import org.example.infrastructure.data.models.HabitTrackEntity;
import org.example.infrastructure.data.repositories.in_memory_repositories.InMemoryHabitRepository;
import org.example.infrastructure.data.repositories.in_memory_repositories.InMemoryHabitTrackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class HabitTrackServiceTest {
    HabitTrackService habitTrackService;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        var tracks = new ArrayList<>(Arrays.asList(
                new HabitTrackEntity(
                        0,
                        0,
                        LocalDate.now()
                ),
                new HabitTrackEntity(
                        1,
                        2,
                        LocalDate.now().minusDays(Period.ofWeeks(0).getDays())
                ),
                new HabitTrackEntity(
                        2,
                        2,
                        LocalDate.now().minusDays(Period.ofWeeks(1).getDays())
                ),
                new HabitTrackEntity(
                        3,
                        2,
                        LocalDate.now().minusDays(Period.ofWeeks(2).getDays())
                ),
                new HabitTrackEntity(
                        4,
                        10,
                        LocalDate.now().minusDays(1)
                )
        )
        );
        InMemoryHabitTrackRepository habitTrackRepository = new InMemoryHabitTrackRepository();
        InMemoryHabitRepository habitRepository = new InMemoryHabitRepository();
        Field field = InMemoryHabitTrackRepository.class.getDeclaredField("tracks");
        field.setAccessible(true);
        field.set(habitTrackRepository, tracks);

        habitTrackService = new HabitTrackService(habitTrackRepository, habitRepository);
    }

    @DisplayName("Check after completeHabit execute HabitTrack was created")
    @Test
    void completeHabit_shouldCreateHabitTrackInMemory_whenExecuteCompleteHabitMethod() {
        habitTrackService.completeHabit(new CreateHabitTrackDto(4));

        var tracks = habitTrackService.getHabitTracks(4);
        assertThat(tracks).hasSize(1);
        var track = tracks.get(0);
        assertThat(track.getHabitId()).isEqualTo(4);
        assertThat(track.getCompleteDate()).isEqualTo(LocalDate.now());
    }

    @DisplayName("Check isCompleteHabit return true when habit is complete")
    @Test
    void isCompleteHabit_shouldReturnTrue_whenHabitIsComplete() {
        Habit habit = new Habit(
                0,
                0,
                "testUser1HabitName",
                "testUser1HabitDescription",
                HabitFrequency.DAILY,
                LocalDate.now().minusDays(1)
        );
        assertThat(habitTrackService.isCompleteHabit(habit)).isTrue();
    }

    @DisplayName("Check isCompleteHabit return false when habit is not complete")
    @Test
    void isCompleteHabit_shouldReturnFalse_whenHabitNotCompleted() {
        Habit habit = new Habit(
                10,
                0,
                "testUser1HabitName",
                "testUser1HabitDescription",
                HabitFrequency.DAILY,
                LocalDate.now()
        );
        assertThat(habitTrackService.isCompleteHabit(habit)).isFalse();
    }

    @DisplayName("Check that getHabitTracks return all habit tracks")
    @Test
    void getHabitTracks_shouldReturnAllHabitTracks() {
        var tracks = habitTrackService.getHabitTracks(2);

        assertThat(tracks)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .allMatch(habitTrack -> habitTrack.getHabitId() == 2);
    }

    @DisplayName("Check that removeHabitTracks remove all habit tracks")
    @Test
    void removeHabitTracks_shouldRemoveHabitFromMemory() {
        habitTrackService.removeHabitTracks(2);

        var tracks = habitTrackService.getHabitTracks(2);
        assertThat(tracks)
                .isNotNull()
                .isEmpty();
    }
}