package org.example.infrastructure.data.repositories.jdbc_repositories;

import org.example.core.models.HabitTrack;
import org.example.core.repositories.habit_track_repository.IHabitTrackRepository;
import org.example.core.repositories.habit_track_repository.dtos.CreateHabitTrackDto;
import org.example.infrastructure.configs.DbConfig;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcHabitTrackRepository implements IHabitTrackRepository {
    private final String url;
    private final String username;
    private final String password;

    public JdbcHabitTrackRepository() throws IOException {
        DbConfig dbConfig = new DbConfig();
        url = dbConfig.getUrl();
        username = dbConfig.getUsername();
        password = dbConfig.getPassword();

    }

    @Override
    public void create(CreateHabitTrackDto dto) {
        String createSql = "INSERT INTO habit_tracker_schema.habit_tracks (habit_id) VALUES (?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
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
        try (Connection connection = DriverManager.getConnection(url, username, password);
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
    public void removeByHabitId(int id) {
        String createSql = "DELETE FROM habit_tracker_schema.habit_tracks WHERE habit_id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(createSql)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
