package org.example.infrastructure.data.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.core.dtos.user_dtos.AuthUserDto;
import org.example.core.dtos.user_dtos.ChangeAdminStatusDto;
import org.example.core.dtos.user_dtos.UpdateUserDto;
import org.example.core.models.User;
import org.example.core.repositories.IUserRepository;
import org.example.exceptions.UserNotFoundException;
import org.example.infrastructure.constants.SqlConstants;
import org.example.infrastructure.util.ConnectionManager;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JdbcUserRepository implements IUserRepository {

    private final ConnectionManager connectionManager;

    private static User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getInt("id"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getBoolean("is_admin")
        );
    }

    @Override
    public User create(AuthUserDto dto) {
        User user = null;
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SqlConstants.CREATE_USER_SQL)) {

            preparedStatement.setString(1, dto.getEmail());
            preparedStatement.setString(2, dto.getPassword());

            preparedStatement.executeUpdate();

            user = getByEmail(dto.getEmail());
        } catch (SQLException | UserNotFoundException e) {
            log.error("Error occurred while trying create user", e);
        }
        return user;
    }

    @Override
    public User getByEmail(String email) throws UserNotFoundException {
        if (email == null) {
            throw new UserNotFoundException();
        }
        User user = null;
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SqlConstants.GET_USER_BY_EMAIL_SQL)) {

            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                user = getUserFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            log.error("Error occurred while trying get user by email", e);
        }
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try (Connection connection = connectionManager.open();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(SqlConstants.GET_ALL_USERS_SQL);

            while (resultSet.next()) {
                users.add(getUserFromResultSet(resultSet)
                );
            }
        } catch (SQLException e) {
            log.error("Error occurred while trying get users", e);
        }
        return users;
    }

    @Override
    public void update(int userId, UpdateUserDto dto) throws UserNotFoundException {
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SqlConstants.UPDATE_USER_SQL)) {

            preparedStatement.setString(1, dto.getEmail());
            preparedStatement.setString(2, dto.getPassword());
            preparedStatement.setInt(3, userId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("Error occurred while trying update users", e);
        }
    }

    @Override
    public void remove(int id) {
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SqlConstants.REMOVE_USER_SQL)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("Error occurred while trying remove users", e);
        }
    }

    @Override
    public void changeUserAdminStatus(int userId, ChangeAdminStatusDto dto) {
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SqlConstants.CHANGE_USER_STATUS_SQL)) {

            preparedStatement.setBoolean(1, dto.isAdmin());
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("Error occurred while trying change user status", e);
        }
    }

    @Override
    public User getById(int id) throws UserNotFoundException {
        User user = null;
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SqlConstants.GET_USER_BY_ID_SQL)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                user = getUserFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            log.error("Error occurred while trying get user by id", e);
        }
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }
}
