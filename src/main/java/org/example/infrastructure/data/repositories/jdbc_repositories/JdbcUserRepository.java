package org.example.infrastructure.data.repositories.jdbc_repositories;

import org.example.core.exceptions.UserNotFoundException;
import org.example.core.models.User;
import org.example.core.repositories.user_repository.IUserRepository;
import org.example.core.repositories.user_repository.dtos.ChangeAdminStatusDto;
import org.example.core.repositories.user_repository.dtos.CreateUserDto;
import org.example.core.repositories.user_repository.dtos.UpdateUserDto;
import org.example.infrastructure.configs.DbConfig;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserRepository implements IUserRepository {
    private final String url;
    private final String username;
    private final String password;

    public JdbcUserRepository() throws IOException {
        DbConfig dbConfig = new DbConfig();
        url = dbConfig.getUrl();
        username = dbConfig.getUsername();
        password = dbConfig.getPassword();

    }

    @Override
    public User create(CreateUserDto dto) {
        String createSql = "INSERT INTO habit_tracker_schema.users (email, password) VALUES (?, ?)";
        User user = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(createSql)) {

            preparedStatement.setString(1, dto.getEmail());
            preparedStatement.setString(2, dto.getPassword());

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
        String createSql = "SELECT * FROM habit_tracker_schema.users WHERE email = ?";
        User user = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(createSql)) {

            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getBoolean("is_admin")
                );
            }

            if (user == null) {
                throw new UserNotFoundException();
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String createSql = "SELECT * FROM habit_tracker_schema.users";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(createSql);

            while (resultSet.next()) {
                users.add(new User(
                                resultSet.getInt("id"),
                                resultSet.getString("email"),
                                resultSet.getString("password"),
                                resultSet.getBoolean("is_admin")
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void update(UpdateUserDto dto) throws UserNotFoundException {
        String createSql = "UPDATE habit_tracker_schema.users SET email = ?, password = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(createSql)) {

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
        String createSql = "DELETE FROM habit_tracker_schema.users WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(createSql)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changeUserAdminStatus(ChangeAdminStatusDto dto) throws UserNotFoundException {
        String createSql = "UPDATE habit_tracker_schema.users SET is_admin = NOT is_admin WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(createSql)) {

            preparedStatement.setInt(1, dto.getUserId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
