package org.example.infrastructure.data.repositories.jdbc_repositories;

import org.example.core.models.Habit;
import org.example.core.models.HabitFrequency;
import org.example.core.repositories.habit_repository.IHabitRepository;
import org.example.core.repositories.habit_repository.dtos.CreateHabitDto;
import org.example.core.repositories.habit_repository.dtos.UpdateHabitDto;
import org.example.infrastructure.configs.DbConfig;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcHabitRepository implements IHabitRepository {

    private final String url;
    private final String username;
    private final String password;
    private final String schemaName;

    public JdbcHabitRepository() throws IOException {
        DbConfig dbConfig = new DbConfig();
        url = dbConfig.getUrl();
        username = dbConfig.getUsername();
        password = dbConfig.getPassword();
        schemaName = dbConfig.getDefaultSchemaName();
    }

    @Override
    public void create(CreateHabitDto dto) {
        String createSql = "INSERT INTO habit_tracker_schema.habits (user_id, name, description, frequency) VALUES (?, ?, ?, ?)";
        System.out.println(String.format("%s %s %s", url, username, password));
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(createSql)) {

            preparedStatement.setInt(1, dto.getUserId());
            preparedStatement.setString(2, dto.getName());
            preparedStatement.setString(3, dto.getDescription());
            preparedStatement.setString(4, dto.getFrequency().toString());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Habit> getAllHabitsByUserId(int userId) {
        List<Habit> habits = new ArrayList<>();
        String createSql = "SELECT * FROM habit_tracker_schema.habits where user_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(createSql)) {

            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                habits.add(new Habit(
                                resultSet.getInt("id"),
                                resultSet.getInt("user_id"),
                                resultSet.getString("name"),
                                resultSet.getString("description"),
                                HabitFrequency.valueOf(resultSet.getString("frequency")),
                                resultSet.getDate("day_of_creation").toLocalDate()
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return habits;
    }

    @Override
    public void update(UpdateHabitDto dto) {
        String createSql = "UPDATE habit_tracker_schema.habits SET name = ?, description = ?, frequency = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(createSql)) {

            preparedStatement.setString(1, dto.getName());
            preparedStatement.setString(2, dto.getDescription());
            preparedStatement.setString(3, dto.getFrequency().toString());
            preparedStatement.setInt(4, dto.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(int id) {
        String createSql = "DELETE FROM habit_tracker_schema.habits WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(createSql)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
