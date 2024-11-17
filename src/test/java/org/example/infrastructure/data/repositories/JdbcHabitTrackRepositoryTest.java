package org.example.infrastructure.data.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(value = "classpath:application.yml")
@Sql(value = {"classpath:test_sql_scripts/remove-all-data.sql", "classpath:test_sql_scripts/insert-test-data.sql"})
class JdbcHabitTrackRepositoryTest {


    @Autowired
    private JdbcHabitTrackRepository habitTrackRepository;

    @DisplayName("Check that create habit working correctly")
    @Test
    void create_shouldCreateHabitTrackInDb() {
        habitTrackRepository.create(2);

        var tracks = habitTrackRepository.getHabitTracks(2);
        assertThat(tracks).hasSize(1);
        var track = tracks.get(0);
        assertThat(track.getHabitId()).isEqualTo(2);
        assertThat(track.getCompleteDate()).isEqualTo(LocalDate.now());
    }

    @DisplayName("Check that getHabitTracks return all habits tracks")
    @Test
    void getHabitTracks() {
        var tracks = habitTrackRepository.getHabitTracks(3);

        assertThat(tracks)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .allMatch(habitTrack -> habitTrack.getHabitId() == 3);
    }

    @DisplayName("Check that remove habit working correctly")
    @Test
    void removeAllByHabitId_shouldRemoveHabitFromDb() {
        habitTrackRepository.removeAllByHabitId(3);

        var tracks = habitTrackRepository.getHabitTracks(3);
        assertThat(tracks)
                .isNotNull()
                .isEmpty();
    }

    @DisplayName("Check that remove habit track working correctly")
    @Test
    void remove_shouldRemoveHabitTrackFromDb() {
        var tracks = habitTrackRepository.getHabitTracks(1);
        assertThat(tracks).anyMatch(habitTrack -> habitTrack.getId() == 1);
        habitTrackRepository.remove(1);

        tracks = habitTrackRepository.getHabitTracks(1);
        assertThat(tracks).allMatch(habitTrack -> habitTrack.getId() != 1);

    }
}