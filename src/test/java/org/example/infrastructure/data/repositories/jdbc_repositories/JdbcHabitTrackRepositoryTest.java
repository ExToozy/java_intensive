package org.example.infrastructure.data.repositories.jdbc_repositories;

import org.example.core.dtos.habit_track_dtos.CreateHabitTrackDto;
import org.example.core.exceptions.ConfigException;
import org.example.infrastructure.configs.DbConfig;
import org.example.infrastructure.migration.MigrationTool;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcHabitTrackRepositoryTest {

    private JdbcHabitTrackRepository habitTrackRepository;
    private Connection connection;

    @BeforeEach
    void setUp() throws ConfigException, SQLException {
        DbConfig dbConfig = new DbConfig();
        connection = DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUsername(), dbConfig.getPassword());
        MigrationTool.runMigrate();
        habitTrackRepository = new JdbcHabitTrackRepository();
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @DisplayName("Check that create habit working correctly")
    @Test
    void create_shouldCreateHabitTrackInDb() {
        habitTrackRepository.create(new CreateHabitTrackDto(2));

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