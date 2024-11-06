package org.example.core.services;

import org.assertj.core.api.SoftAssertions;
import org.example.core.dtos.user_dtos.AuthUserDto;
import org.example.core.models.User;
import org.example.core.util.PasswordManager;
import org.example.exceptions.InvalidEmailException;
import org.example.exceptions.UserAlreadyExistException;
import org.example.exceptions.UserNotFoundException;
import org.example.infrastructure.configs.DbConfig;
import org.example.infrastructure.data.repositories.JdbcUserRepository;
import org.example.infrastructure.migration.MigrationTool;
import org.example.infrastructure.util.ConnectionManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        DbConfig.class,
        AuthService.class,
        UserService.class,
        JdbcUserRepository.class,
        ConnectionManager.class
})
@TestPropertySource(locations = "classpath:application-test.properties")
class AuthServiceTest {
    @Autowired
    DbConfig dbConfig;

    @Autowired
    AuthService authService;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        ConnectionManager connectionManager = new ConnectionManager(dbConfig);
        connection = connectionManager.open();
        new MigrationTool(dbConfig, connectionManager).runMigrate();
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @DisplayName("Check login with correct credentials")
    @Test
    void login_shouldGetUser_whenCredentialsIsCorrect() throws UserNotFoundException {
        User user = authService.login(new AuthUserDto("ex@mail.ru", "123"));
        assertThat(user.getId()).isEqualTo(1);
    }

    @DisplayName("Check login with invalid credentials")
    @Test
    void login_shouldThrowException_whenCredentialsIsInvalid() {
        assertThatThrownBy(() -> authService.login(new AuthUserDto("ex@mail.ru", "1234"))).isInstanceOf(UserNotFoundException.class);
        assertThatThrownBy(() -> authService.login(new AuthUserDto("notExistedEmail@mail", "123"))).isInstanceOf(UserNotFoundException.class);
    }

    @DisplayName("Check register with valid email")
    @Test
    void register_shouldGetUser_whenEmailIsValid() throws UserAlreadyExistException, InvalidEmailException {
        User user = authService.register(new AuthUserDto("newUser@mail.ru", "123"));

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(user.getPassword()).isEqualTo(PasswordManager.getPasswordHash("123"));
        softly.assertThat(user.getEmail()).isEqualTo("newUser@mail.ru");
        softly.assertThat(user.getId()).isNotNull();
        softly.assertAll();
    }

    @DisplayName("Check register with invalid email")
    @ParameterizedTest
    @ValueSource(strings = {"mail", "mail.ru", "mail@mail", "$#", "", "mail@mail.fgdgdfgdfgdf"})
    void register_shouldThrowException_whenEmailIsInvalid(String email) {
        assertThatThrownBy(() -> authService.register(new AuthUserDto(email, "123"))).isInstanceOf(InvalidEmailException.class);
    }

    @DisplayName("Check register with already existed user")
    @Test
    void register_shouldThrowException_whenUserIsAlreadyExist() {
        assertThatThrownBy(() -> authService.register(new AuthUserDto("ex@mail.ru", "123"))).isInstanceOf(UserAlreadyExistException.class);
    }
}