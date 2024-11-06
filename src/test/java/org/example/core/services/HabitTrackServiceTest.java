package org.example.core.services;

import org.assertj.core.api.SoftAssertions;
import org.example.infrastructure.configs.DbConfig;
import org.example.infrastructure.data.repositories.JdbcHabitRepository;
import org.example.infrastructure.data.repositories.JdbcHabitTrackRepository;
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
@ContextConfiguration(classes = {
        DbConfig.class,
        HabitTrackService.class,
        JdbcHabitRepository.class,
        JdbcHabitTrackRepository.class,
        ConnectionManager.class
})
@TestPropertySource(locations = "classpath:application-test.properties")
class HabitTrackServiceTest {
    @Autowired
    HabitTrackService habitTrackService;

    @Autowired
    DbConfig dbConfig;

    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        ConnectionManager connectionManager = new ConnectionManager(dbConfig);
        connection = connectionManager.open();
        new MigrationTool(dbConfig, connectionManager).runMigrate();
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @DisplayName("Check after completeHabit execute HabitTrack was created")
    @Test
    void completeHabit_shouldCreateHabitTrackInMemory_whenExecuteCompleteHabitMethod() {
        habitTrackService.completeHabit(2);

        SoftAssertions softly = new SoftAssertions();
        var tracks = habitTrackService.getHabitTracks(2);
        softly.assertThat(tracks).hasSize(1);
        var track = tracks.get(0);
        softly.assertThat(track.getHabitId()).isEqualTo(2);
        softly.assertThat(track.getCompleteDate()).isEqualTo(LocalDate.now());
        softly.assertAll();
    }

    @DisplayName("Check that getHabitTracks return all habit tracks")
    @Test
    void getHabitTracks_shouldReturnAllHabitTracks() {
        var tracks = habitTrackService.getHabitTracks(3);

        assertThat(tracks)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .allMatch(habitTrack -> habitTrack.getHabitId() == 3);
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