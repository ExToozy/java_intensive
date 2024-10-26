package org.example.infrastructure.data.repositories.jdbc_repositories;

import org.example.core.dtos.habit_track_dtos.CreateHabitTrackDto;
import org.example.core.models.HabitTrack;
import org.example.core.repositories.IHabitTrackRepository;
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
        String createSql = "INSERT INTO habit_tracker_schema.habit_tracks (habit_id) VALUES (?)";

        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(createSql)) {

            preparedStatement.setInt(1, dto.getHabitId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<HabitTrack> getHabitTracks(int habitId) {
        List<HabitTrack> tracks = new ArrayList<>();
        String createSql = "SELECT * FROM habit_tracker_schema.habit_tracks where habit_id = ?";
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(createSql)) {

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
        String createSql = "DELETE FROM habit_tracker_schema.habit_tracks WHERE habit_id = ?";

        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(createSql)) {

            preparedStatement.setInt(1, habitId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(int id) {
        String createSql = "DELETE FROM habit_tracker_schema.habit_tracks WHERE id = ?";

        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(createSql)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
