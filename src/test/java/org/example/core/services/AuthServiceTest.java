package org.example.core.services;

import io.jsonwebtoken.Claims;
import org.assertj.core.api.SoftAssertions;
import org.example.core.dtos.auth_dtos.AuthDto;
import org.example.core.dtos.user_dtos.AuthUserDto;
import org.example.exceptions.InvalidEmailException;
import org.example.exceptions.UserAlreadyExistException;
import org.example.exceptions.UserNotFoundException;
import org.example.infrastructure.util.JwtProvider;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(value = "classpath:application.yml")
@Sql(value = {"classpath:test_sql_scripts/remove-all-data.sql", "classpath:test_sql_scripts/insert-test-data.sql"})
class AuthServiceTest {

    @Autowired
    AuthService authService;

    @Autowired
    JwtProvider jwtProvider;

    @DisplayName("Check login with correct credentials")
    @Test
    void login_shouldGetUser_whenCredentialsIsCorrect() throws UserNotFoundException {
        AuthDto authDto = authService.login(new AuthUserDto("ex@mail.ru", "123"));

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(authDto).isNotNull();
        softly.assertThat(authDto.getToken()).isNotNull();
        Claims claims = jwtProvider.getClaims(authDto.getToken());
        softly.assertThat(claims).containsEntry("user_id", 1).containsEntry("is_admin", false);
        softly.assertAll();
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
        AuthDto authDto = authService.register(new AuthUserDto("newUser@mail.ru", "123"));

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(authDto).isNotNull();
        softly.assertThat(authDto.getToken()).isNotNull();
        Claims claims = jwtProvider.getClaims(authDto.getToken());
        softly.assertThat(claims).containsEntry("user_id", 4).containsEntry("is_admin", false);
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