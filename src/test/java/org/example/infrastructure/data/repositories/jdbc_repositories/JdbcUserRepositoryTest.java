package org.example.infrastructure.data.repositories.jdbc_repositories;

import org.example.core.dtos.user_dtos.AuthUserDto;
import org.example.core.dtos.user_dtos.ChangeAdminStatusDto;
import org.example.core.dtos.user_dtos.UpdateUserDto;
import org.example.core.exceptions.ConfigException;
import org.example.core.exceptions.UserNotFoundException;
import org.example.core.util.PasswordManager;
import org.example.infrastructure.configs.DbConfig;
import org.example.infrastructure.migration.MigrationTool;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JdbcUserRepositoryTest {
    private JdbcUserRepository userRepository;
    private Connection connection;

    @BeforeEach
    void setUp() throws ConfigException, SQLException {
        DbConfig dbConfig = new DbConfig();
        connection = DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUsername(), dbConfig.getPassword());
        MigrationTool.runMigrate();
        userRepository = new JdbcUserRepository();
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @DisplayName("Check create working correctly")
    @Test
    void create_shouldCreateUserInDb() throws UserNotFoundException {
        userRepository.create(new AuthUserDto("test@mail.ru", "123"));

        var user = userRepository.getByEmail("test@mail.ru");

        assertThat(user).isNotNull();
        assertThat(user.getPassword()).isEqualTo("123");
        assertThat(user.getId()).isNotNull();
    }

    @DisplayName("Check getByEmail working correctly")
    @Test
    void getByEmail_shouldReturnUser_whenUserExist() throws UserNotFoundException {
        var user = userRepository.getByEmail("ex@mail.ru");

        assertThat(user).isNotNull();
        assertThat(user.getPassword()).isEqualTo(PasswordManager.getPasswordHash("123"));
        assertThat(user.getId()).isNotNull();
    }

    @Test
    void getById_shouldReturnUser_whenUserExist() throws UserNotFoundException {
        var user = userRepository.getById(1);

        assertThat(user).isNotNull();
        assertThat(user.getPassword()).isEqualTo(PasswordManager.getPasswordHash("123"));
        assertThat(user.getId()).isNotNull();
    }

    @DisplayName("Check getByEmail throws exception if user not exist")
    @Test
    void getByEmail_shouldThrowException_whenUserNotExist() {
        assertThatThrownBy(() -> userRepository.getByEmail("test@test")).isInstanceOf(UserNotFoundException.class);
    }

    @DisplayName("Check getAll return all users")
    @Test
    void getAll_shouldReturnAllUsers() {
        var users = userRepository.getAll();

        assertThat(users)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .allMatch(user -> user.getEmail() != null && user.getPassword() != null);
    }

    @DisplayName("Check update working correctly")
    @Test
    void update_shouldUpdateUser_whenUserExist() throws UserNotFoundException {
        userRepository.update(new UpdateUserDto(1, "new@mail.ru", "1234"));

        assertThatThrownBy(() -> userRepository.getByEmail("ex@mail.ru")).isInstanceOf(UserNotFoundException.class);

        var user = userRepository.getByEmail("new@mail.ru");
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getEmail()).isEqualTo("new@mail.ru");
        assertThat(user.getPassword()).isEqualTo("1234");
    }

    @DisplayName("Check remove working correctly")
    @Test
    void remove_shouldRemove_whenUserExist() {
        userRepository.remove(3);
        assertThatThrownBy(() -> userRepository.getByEmail("test")).isInstanceOf(UserNotFoundException.class);
    }

    @DisplayName("Check changeUserAdminStatus to true")
    @Test
    void changeUserAdminStatus_shouldChangeToTrue_whenProvideTrue() throws UserNotFoundException {
        userRepository.changeUserAdminStatus(new ChangeAdminStatusDto(1, true));
        var user = userRepository.getByEmail("ex@mail.ru");
        assertThat(user.isAdmin()).isTrue();
    }

    @DisplayName("Check changeUserAdminStatus to false")
    @Test
    void changeUserAdminStatus_shouldChangeToFalse_whenProvideFalse() throws UserNotFoundException {
        userRepository.changeUserAdminStatus(new ChangeAdminStatusDto(2, false));
        var user = userRepository.getByEmail("admin");
        assertThat(user.isAdmin()).isFalse();
    }
}