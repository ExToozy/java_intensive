package org.example.core.services;

import org.example.core.models.Habit;
import org.example.core.models.HabitFrequency;
import org.example.core.repositories.habit_track_repository.dtos.CreateHabitTrackDto;
import org.example.infastructure.data.models.HabitTrackEntity;
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

class HabitTrackServiceTest {
    HabitTrackService habitTrackService;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
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
        InMemoryHabitTrackRepository habitTrackRepository = new InMemoryHabitTrackRepository();
        Field field = InMemoryHabitTrackRepository.class.getDeclaredField("tracks");
        field.setAccessible(true);
        field.set(habitTrackRepository, tracks);
        habitTrackService = new HabitTrackService(habitTrackRepository);
    }

    @DisplayName("Check after completeHabit execute HabitTrack was created")
    @Test
    void completeHabit_shouldCreateHabitTrackInMemory_whenExecuteCompleteHabitMethod() {
        habitTrackService.completeHabit(new CreateHabitTrackDto(UUID.fromString("00000000-0000-0000-0000-000000000004")));

        var tracks = habitTrackService.getHabitTracks(UUID.fromString("00000000-0000-0000-0000-000000000004"));
        assertThat(tracks).hasSize(1);
        var track = tracks.get(0);
        assertThat(track.getHabitId()).isEqualTo(UUID.fromString("00000000-0000-0000-0000-000000000004"));
        assertThat(track.getCompleteDate()).isEqualTo(LocalDate.now());
    }

    @DisplayName("Check isCompleteHabit return true when habit is complete")
    @Test
    void isCompleteHabit_shouldReturnTrue_whenHabitIsComplete() {
        Habit habit = new Habit(
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
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
                UUID.fromString("00000000-0000-0000-0000-000000000010"),
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
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
        var tracks = habitTrackService.getHabitTracks(UUID.fromString("00000000-0000-0000-0000-000000000002"));

        assertThat(tracks)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .allMatch(habitTrack -> habitTrack.getHabitId().equals(UUID.fromString("00000000-0000-0000-0000-000000000002")));
    }

    @DisplayName("Check that removeHabitTracks remove all habit tracks")
    @Test
    void removeHabitTracks_shouldRemoveHabitFromMemory() {
        habitTrackService.removeHabitTracks(UUID.fromString("00000000-0000-0000-0000-000000000002"));

        var tracks = habitTrackService.getHabitTracks(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        assertThat(tracks)
                .isNotNull()
                .isEmpty();
    }
}