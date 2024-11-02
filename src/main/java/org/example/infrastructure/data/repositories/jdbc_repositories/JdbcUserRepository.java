package org.example.infrastructure.data.repositories.jdbc_repositories;

import org.example.core.dtos.user_dtos.AuthUserDto;
import org.example.core.dtos.user_dtos.ChangeAdminStatusDto;
import org.example.core.dtos.user_dtos.UpdateUserDto;
import org.example.core.exceptions.UserNotFoundException;
import org.example.core.models.User;
import org.example.core.repositories.IUserRepository;
import org.example.infrastructure.constants.SqlConstants;
import org.example.infrastructure.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserRepository implements IUserRepository {

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
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SqlConstants.CREATE_USER_SQL)) {

            preparedStatement.setString(1, dto.getEmail());
            preparedStatement.setString(2, dto.getPassword());

            preparedStatement.executeUpdate();

            user = getByEmail(dto.getEmail());
        } catch (SQLException | UserNotFoundException e) {
            e.printStackTrace(System.err);
        }
        return user;
    }

    @Override
    public User getByEmail(String email) throws UserNotFoundException {
        if (email == null) {
            throw new UserNotFoundException();
        }
        User user = null;
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SqlConstants.GET_USER_BY_EMAIL_SQL)) {

            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                user = getUserFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try (Connection connection = ConnectionManager.open();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(SqlConstants.GET_ALL_USERS_SQL);

            while (resultSet.next()) {
                users.add(getUserFromResultSet(resultSet)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void update(UpdateUserDto dto) throws UserNotFoundException {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SqlConstants.UPDATE_USER_SQL)) {

            preparedStatement.setString(1, dto.getEmail());
            preparedStatement.setString(2, dto.getPassword());
            preparedStatement.setInt(3, dto.getUserId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(int id) {

        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SqlConstants.REMOVE_USER_SQL)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changeUserAdminStatus(ChangeAdminStatusDto dto) throws UserNotFoundException {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SqlConstants.CHANGE_USER_STATUS_SQL)) {

            preparedStatement.setBoolean(1, dto.isAdmin());
            preparedStatement.setInt(2, dto.getUserId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getById(int id) throws UserNotFoundException {
        User user = null;
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SqlConstants.GET_USER_BY_ID_SQL)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                user = getUserFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }
}
