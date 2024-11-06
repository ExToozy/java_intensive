package org.example.infrastructure.data.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.core.dtos.habit_dtos.CreateHabitDto;
import org.example.core.dtos.habit_dtos.UpdateHabitDto;
import org.example.core.models.Habit;
import org.example.core.models.HabitFrequency;
import org.example.core.repositories.IHabitRepository;
import org.example.exceptions.HabitNotFoundException;
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
public class JdbcHabitRepository implements IHabitRepository {

    private final ConnectionManager connectionManager;

    private static Habit getHabitFromResultSet(ResultSet resultSet) throws SQLException {
        return new Habit(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                HabitFrequency.valueOf(resultSet.getString("frequency")),
                resultSet.getDate("day_of_creation").toLocalDate()
        );
    }

    @Override
    public void create(CreateHabitDto dto) {
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SqlConstants.CREATE_HABIT_SQL)) {

            preparedStatement.setInt(1, dto.getUserId());
            preparedStatement.setString(2, dto.getName());
            preparedStatement.setString(3, dto.getDescription());
            preparedStatement.setString(4, dto.getFrequency());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("Error occurred while trying create habit", e);
        }
    }

    @Override
    public List<Habit> getAllHabitsByUserId(int userId) {
        List<Habit> habits = new ArrayList<>();
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SqlConstants.GET_USER_HABITS_SQL)) {

            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                habits.add(getHabitFromResultSet(resultSet)
                );
            }
        } catch (SQLException e) {
            log.error("Error occurred while trying get habits", e);
        }
        return habits;
    }

    @Override
    public Habit getHabitById(int habitId) throws HabitNotFoundException {
        Habit habit = null;
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SqlConstants.GET_USER_HABIT_BY_ID_SQL)) {

            preparedStatement.setInt(1, habitId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                habit = getHabitFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            log.error("Error occurred while trying get habit by id", e);
        }
        if (habit == null) {
            throw new HabitNotFoundException();
        }
        return habit;
    }

    @Override
    public void update(int habitId, UpdateHabitDto dto) {

        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SqlConstants.UPDATE_HABIT_SQL)) {

            preparedStatement.setString(1, dto.getName());
            preparedStatement.setString(2, dto.getDescription());
            preparedStatement.setString(3, dto.getFrequency());
            preparedStatement.setInt(4, habitId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("Error occurred while trying update habit", e);
        }
    }

    @Override
    public void remove(int id) {

        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SqlConstants.REMOVE_HABIT_SQL)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("Error occurred while trying remove habit", e);
        }
    }
}
