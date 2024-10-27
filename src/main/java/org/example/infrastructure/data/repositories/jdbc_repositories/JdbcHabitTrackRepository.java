package org.example.infrastructure.data.repositories.jdbc_repositories;

import org.example.core.dtos.habit_track_dtos.CreateHabitTrackDto;
import org.example.core.models.HabitTrack;
import org.example.core.repositories.IHabitTrackRepository;
import org.example.infrastructure.constants.SqlConstants;
import org.example.infrastructure.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcHabitTrackRepository implements IHabitTrackRepository {


    @Override
    public void create(CreateHabitTrackDto dto) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SqlConstants.CREATE_HABIT_TRACK_SQL)) {

            preparedStatement.setInt(1, dto.getHabitId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<HabitTrack> getHabitTracks(int habitId) {
        List<HabitTrack> tracks = new ArrayList<>();
        try (Connection connection = ConnectionManager.open();
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
            e.printStackTrace();
        }
        return tracks;
    }

    @Override
    public void removeAllByHabitId(int habitId) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SqlConstants.REMOVE_ALL_HABIT_TRACK_SQL)) {

            preparedStatement.setInt(1, habitId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(int id) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SqlConstants.REMOVE_HABIT_TRACK_SQL)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
