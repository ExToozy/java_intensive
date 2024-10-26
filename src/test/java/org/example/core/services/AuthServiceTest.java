package org.example.core.services;

import org.example.core.dtos.user_dtos.AuthUserDto;
import org.example.core.exceptions.InvalidEmailException;
import org.example.core.exceptions.UserAlreadyExistException;
import org.example.core.exceptions.UserNotFoundException;
import org.example.core.models.User;
import org.example.core.util.PasswordManager;
import org.example.infrastructure.data.models.UserEntity;
import org.example.infrastructure.data.repositories.in_memory_repositories.InMemoryHabitRepository;
import org.example.infrastructure.data.repositories.in_memory_repositories.InMemoryHabitTrackRepository;
import org.example.infrastructure.data.repositories.in_memory_repositories.InMemoryUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class AuthServiceTest {
    AuthService authService;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        var users = new ArrayList<>(Arrays.asList(
                new UserEntity(
                        0,
                        "ex@mail.ru",
                        PasswordManager.getPasswordHash("123"),
                        false
                ),
                new UserEntity(
                        1,
                        "admin",
                        PasswordManager.getPasswordHash("admin"),
                        true
                )
        )
        );
        InMemoryUserRepository userRepository = new InMemoryUserRepository();
        InMemoryHabitRepository habitRepository = new InMemoryHabitRepository();
        InMemoryHabitTrackRepository habitTrackRepository = new InMemoryHabitTrackRepository();
        HabitTrackService habitTrackService = new HabitTrackService(habitTrackRepository, habitRepository);
        HabitService habitService = new HabitService(habitRepository, habitTrackService);
        Field field = InMemoryUserRepository.class.getDeclaredField("users");
        field.setAccessible(true);
        field.set(userRepository, users);
        UserService userService = new UserService(userRepository, habitService);
        authService = new AuthService(userService);
    }

    @DisplayName("Check login with correct credentials")
    @Test
    void login_shouldGetUser_whenCredentialsIsCorrect() throws UserNotFoundException {
        User user = authService.login(new AuthUserDto("ex@mail.ru", "123"));
        assertThat(user.getId()).isEqualTo(0);
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
        assertThat(user.getPassword()).isEqualTo(PasswordManager.getPasswordHash("123"));
        assertThat(user.getEmail()).isEqualTo("newUser@mail.ru");
        assertThat(user.getId()).isNotNull();
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