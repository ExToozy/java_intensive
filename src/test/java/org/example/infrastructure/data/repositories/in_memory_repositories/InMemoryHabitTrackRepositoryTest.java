package org.example.infrastructure.data.repositories.in_memory_repositories;

import org.example.core.dtos.habit_track_dtos.CreateHabitTrackDto;
import org.example.infrastructure.data.models.HabitTrackEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryHabitTrackRepositoryTest {
    InMemoryHabitTrackRepository habitTrackRepository;

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
        habitTrackRepository.create(new CreateHabitTrackDto(4));

        var tracks = habitTrackRepository.getHabitTracks(4);
        assertThat(tracks).hasSize(1);
        var track = tracks.get(0);
        assertThat(track.getHabitId()).isEqualTo(4);
        assertThat(track.getCompleteDate()).isEqualTo(LocalDate.now());
    }

    @DisplayName("Check that getHabitTracks return all habits tracks")
    @Test
    void getHabitTracks_shouldReturnAllHabitTracks() {
        var tracks = habitTrackRepository.getHabitTracks(2);

        assertThat(tracks)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .allMatch(habitTrack -> habitTrack.getHabitId() == 2);
    }

    @DisplayName("Check that remove habit working correctly")
    @Test
    void removeByHabitId_shouldRemoveHabitFromMemory() {
        habitTrackRepository.removeAllByHabitId(2);

        var tracks = habitTrackRepository.getHabitTracks(2);
        assertThat(tracks)
                .isNotNull()
                .isEmpty();
    }
}