package org.example.infastructure.data.repositories.in_memory_repositories;

import org.example.core.repositories.habit_track_repository.dtos.CreateHabitTrackDto;
import org.example.infastructure.data.models.HabitTrackEntity;
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

class InMemoryHabitTrackRepositoryTest {
    InMemoryHabitTrackRepository habitTrackRepository;

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
                )
        )
        );
        habitTrackRepository = new InMemoryHabitTrackRepository();
        Field field = InMemoryHabitTrackRepository.class.getDeclaredField("tracks");
        field.setAccessible(true);
        field.set(habitTrackRepository, tracks);
    }

    @DisplayName("Check that create habit working correctly")
    @Test
    void create_shouldCreateHabitTrackInMemory() {
        habitTrackRepository.create(new CreateHabitTrackDto(UUID.fromString("00000000-0000-0000-0000-000000000004")));

        var tracks = habitTrackRepository.getHabitTracks(UUID.fromString("00000000-0000-0000-0000-000000000004"));
        assertThat(tracks).hasSize(1);
        var track = tracks.get(0);
        assertThat(track.getHabitId()).isEqualTo(UUID.fromString("00000000-0000-0000-0000-000000000004"));
        assertThat(track.getCompleteDate()).isEqualTo(LocalDate.now());
    }

    @DisplayName("Check that getHabitTracks return all habits tracks")
    @Test
    void getHabitTracks_shouldReturnAllHabitTracks() {
        var tracks = habitTrackRepository.getHabitTracks(UUID.fromString("00000000-0000-0000-0000-000000000002"));

        assertThat(tracks)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .allMatch(habitTrack -> habitTrack.getHabitId().equals(UUID.fromString("00000000-0000-0000-0000-000000000002")));
    }

    @DisplayName("Check that remove habit working correctly")
    @Test
    void removeByHabitId_shouldRemoveHabitFromMemory() {
        habitTrackRepository.removeByHabitId(UUID.fromString("00000000-0000-0000-0000-000000000002"));

        var tracks = habitTrackRepository.getHabitTracks(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        assertThat(tracks)
                .isNotNull()
                .isEmpty();
    }
}