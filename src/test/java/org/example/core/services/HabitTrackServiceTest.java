package org.example.core.services;

import org.assertj.core.api.SoftAssertions;
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
class HabitTrackServiceTest {
    @Autowired
    HabitTrackService habitTrackService;


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