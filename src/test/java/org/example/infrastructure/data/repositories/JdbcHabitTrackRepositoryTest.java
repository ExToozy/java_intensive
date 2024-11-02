package org.example.infrastructure.data.repositories;

import org.example.infrastructure.configs.DbConfig;
import org.example.infrastructure.migration.MigrationTool;
import org.example.infrastructure.util.ConnectionManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DbConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class JdbcHabitTrackRepositoryTest {

    @Autowired
    private DbConfig dbConfig;
    private JdbcHabitTrackRepository habitTrackRepository;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        ConnectionManager connectionManager = new ConnectionManager(dbConfig);
        connection = connectionManager.open();
        new MigrationTool(dbConfig, connectionManager).runMigrate();
        habitTrackRepository = new JdbcHabitTrackRepository(connectionManager);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

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