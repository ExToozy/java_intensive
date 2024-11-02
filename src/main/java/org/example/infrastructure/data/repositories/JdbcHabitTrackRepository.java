package org.example.infrastructure.data.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.core.models.HabitTrack;
import org.example.core.repositories.IHabitTrackRepository;
import org.example.infrastructure.constants.SqlConstants;
import org.example.infrastructure.util.ConnectionManager;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JdbcHabitTrackRepository implements IHabitTrackRepository {

    private final ConnectionManager connectionManager;

    @Override
    public void create(int habitId) {
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SqlConstants.CREATE_HABIT_TRACK_SQL)) {

            preparedStatement.setInt(1, habitId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("Error occurred while trying create habit track", e);
        }
    }

    @Override
    public List<HabitTrack> getHabitTracks(int habitId) {
        List<HabitTrack> tracks = new ArrayList<>();
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SqlConstants.GET_HABIT_TRACKS_SQL)) {

            preparedStatement.setInt(1, habitId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                tracks.add(new HabitTrack(
                                resultSet.getInt("id"),
                                resultSet.getInt("habit_id"),
                                resultSet.getDate("complete_date").toLocalDate()
                        )
                );
            }
        } catch (SQLException e) {
            log.error("Error occurred while trying get habit tracks", e);
        }
        return tracks;
    }

    @Override
    public void removeAllByHabitId(int habitId) {
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SqlConstants.REMOVE_ALL_HABIT_TRACK_SQL)) {

            preparedStatement.setInt(1, habitId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("Error occurred while trying remove habit tracks", e);
        }
    }

    @Override
    public void remove(int id) {
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SqlConstants.REMOVE_HABIT_TRACK_SQL)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("Error occurred while trying remove habit track", e);
        }
    }
}
