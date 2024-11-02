package org.example.infrastructure.data.repositories;

import org.assertj.core.api.SoftAssertions;
import org.example.core.dtos.user_dtos.AuthUserDto;
import org.example.core.dtos.user_dtos.ChangeAdminStatusDto;
import org.example.core.dtos.user_dtos.UpdateUserDto;
import org.example.core.util.PasswordManager;
import org.example.exceptions.UserNotFoundException;
import org.example.infrastructure.configs.DbConfig;
import org.example.infrastructure.migration.MigrationTool;
import org.example.infrastructure.util.ConnectionManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DbConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class JdbcUserRepositoryTest {
    @Autowired
    private DbConfig dbConfig;
    private JdbcUserRepository userRepository;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        ConnectionManager connectionManager = new ConnectionManager(dbConfig);
        connection = connectionManager.open();
        new MigrationTool(dbConfig, connectionManager).runMigrate();
        userRepository = new JdbcUserRepository(connectionManager);
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

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(user).isNotNull();
        softly.assertThat(user.getPassword()).isEqualTo("123");
        softly.assertThat(user.getId()).isNotNull();
        softly.assertAll();
    }

    @DisplayName("Check getByEmail working correctly")
    @Test
    void getByEmail_shouldReturnUser_whenUserExist() throws UserNotFoundException {
        var user = userRepository.getByEmail("ex@mail.ru");

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(user).isNotNull();
        softly.assertThat(user.getPassword()).isEqualTo(PasswordManager.getPasswordHash("123"));
        softly.assertThat(user.getId()).isNotNull();
        softly.assertAll();
    }

    @Test
    void getById_shouldReturnUser_whenUserExist() throws UserNotFoundException {
        var user = userRepository.getById(1);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(user).isNotNull();
        softly.assertThat(user.getPassword()).isEqualTo(PasswordManager.getPasswordHash("123"));
        softly.assertThat(user.getId()).isNotNull();
        softly.assertAll();
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
        userRepository.update(1, new UpdateUserDto("new@mail.ru", "1234"));

        assertThatThrownBy(() -> userRepository.getByEmail("ex@mail.ru")).isInstanceOf(UserNotFoundException.class);

        var user = userRepository.getByEmail("new@mail.ru");

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(user.getId()).isEqualTo(1);
        softly.assertThat(user.getEmail()).isEqualTo("new@mail.ru");
        softly.assertThat(user.getPassword()).isEqualTo("1234");
        softly.assertAll();
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
        userRepository.changeUserAdminStatus(1, new ChangeAdminStatusDto(true));
        var user = userRepository.getByEmail("ex@mail.ru");
        assertThat(user.isAdmin()).isTrue();
    }

    @DisplayName("Check changeUserAdminStatus to false")
    @Test
    void changeUserAdminStatus_shouldChangeToFalse_whenProvideFalse() throws UserNotFoundException {
        userRepository.changeUserAdminStatus(2, new ChangeAdminStatusDto(false));
        var user = userRepository.getByEmail("admin");
        assertThat(user.isAdmin()).isFalse();
    }
}