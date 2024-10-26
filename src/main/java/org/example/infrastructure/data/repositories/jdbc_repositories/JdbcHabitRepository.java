package org.example.infrastructure.data.repositories.jdbc_repositories;

import org.example.core.dtos.habit_dtos.CreateHabitDto;
import org.example.core.dtos.habit_dtos.UpdateHabitDto;
import org.example.core.models.Habit;
import org.example.core.models.HabitFrequency;
import org.example.core.repositories.IHabitRepository;
import org.example.infrastructure.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcHabitRepository implements IHabitRepository {

    @Override
    public void create(CreateHabitDto dto) {
        String createSql = "INSERT INTO habit_tracker_schema.habits (user_id, name, description, frequency) VALUES (?, ?, ?, ?)";
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(createSql)) {

            preparedStatement.setInt(1, dto.userId());
            preparedStatement.setString(2, dto.name());
            preparedStatement.setString(3, dto.description());
            preparedStatement.setString(4, dto.frequency().toString());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Habit> getAllHabitsByUserId(int userId) {
        List<Habit> habits = new ArrayList<>();
        String createSql = "SELECT * FROM habit_tracker_schema.habits where user_id = ?";
        try (Connection connection = ConnectionManager.open();
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

        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(createSql)) {

            preparedStatement.setString(1, dto.name());
            preparedStatement.setString(2, dto.description());
            preparedStatement.setString(3, dto.frequency().toString());
            preparedStatement.setInt(4, dto.id());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(int id) {
        String createSql = "DELETE FROM habit_tracker_schema.habits WHERE id = ?";

        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(createSql)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
