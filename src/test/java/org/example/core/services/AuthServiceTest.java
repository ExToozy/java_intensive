package org.example.core.services;

import org.assertj.core.api.SoftAssertions;
import org.example.core.dtos.user_dtos.AuthUserDto;
import org.example.core.models.User;
import org.example.core.util.PasswordManager;
import org.example.exceptions.InvalidEmailException;
import org.example.exceptions.UserAlreadyExistException;
import org.example.exceptions.UserNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(value = "classpath:application.yml")
@Sql(value = {"classpath:test_sql_scripts/remove-all-data.sql", "classpath:test_sql_scripts/insert-test-data.sql"})
class AuthServiceTest {

    @Autowired
    AuthService authService;

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