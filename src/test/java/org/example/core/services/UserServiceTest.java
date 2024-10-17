package org.example.core.services;

import org.example.core.exceptions.InvalidEmailException;
import org.example.core.exceptions.UserNotFoundException;
import org.example.core.repositories.user_repository.dtos.ChangeAdminStatusDto;
import org.example.core.repositories.user_repository.dtos.CreateUserDto;
import org.example.core.repositories.user_repository.dtos.UpdateUserDto;
import org.example.core.util.PasswordManager;
import org.example.infastructure.data.models.UserEntity;
import org.example.infastructure.data.repositories.in_memory_repositories.InMemoryHabitRepository;
import org.example.infastructure.data.repositories.in_memory_repositories.InMemoryHabitTrackRepository;
import org.example.infastructure.data.repositories.in_memory_repositories.InMemoryUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserServiceTest {
    UserService userService;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        var users = new ArrayList<>(Arrays.asList(
                new UserEntity(
                        UUID.fromString("00000000-0000-0000-0000-000000000000"),
                        "ex@mail.ru",
                        PasswordManager.getPasswordHash("123"),
                        false
                ),
                new UserEntity(
                        UUID.fromString("00000000-0000-0000-0000-000000000001"),
                        "admin",
                        PasswordManager.getPasswordHash("admin"),
                        true
                )
        )
        );
        InMemoryUserRepository userRepository = new InMemoryUserRepository();
        InMemoryHabitRepository habitRepository = new InMemoryHabitRepository();
        InMemoryHabitTrackRepository habitTrackRepository = new InMemoryHabitTrackRepository();
        HabitTrackService habitTrackService = new HabitTrackService(habitTrackRepository);
        HabitService habitService = new HabitService(habitRepository, habitTrackService);
        Field field = InMemoryUserRepository.class.getDeclaredField("users");
        field.setAccessible(true);
        field.set(userRepository, users);
        userService = new UserService(userRepository, habitService);
    }

    @DisplayName("Check that create successfully create user")
    @Test
    void create_shouldCreateUser() throws UserNotFoundException {
        userService.create(new CreateUserDto("test@mail.ru", "123"));

        var user = userService.getUserByEmail("test@mail.ru");

        assertThat(user).isNotNull();
        assertThat(user.getPassword()).isEqualTo("123");
        assertThat(user.getId()).isNotNull();
    }

    @DisplayName("Check getUserByEmail return user by email correctly")
    @Test
    void getUserByEmail_shouldGetCorrectUser() throws UserNotFoundException {
        var user = userService.getUserByEmail("ex@mail.ru");

        assertThat(user).isNotNull();
        assertThat(user.getPassword()).isEqualTo(PasswordManager.getPasswordHash("123"));
        assertThat(user.getId()).isNotNull();
    }

    @DisplayName("Check getUserByEmail throw exception when user not exist")
    @Test
    void getUserByEmail_shouldThrowException_whenUserWithRequestedEmailNotExits() {
        assertThatThrownBy(() -> userService.getUserByEmail("test@test")).isInstanceOf(UserNotFoundException.class);
    }

    @DisplayName("Check getAll return all users from memory")
    @Test
    void getAll_shouldReturnAllUsersInMemory() {
        var users = userService.getAll();

        assertThat(users)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .allMatch(user -> user.getId() != null && user.getEmail() != null && user.getPassword() != null);
    }

    @DisplayName("Check update user working is correct")
    @Test
    void update_shouldUpdateUser_whenEmailIsCorrectAndUserExist() throws UserNotFoundException, InvalidEmailException {
        userService.update(new UpdateUserDto(UUID.fromString("00000000-0000-0000-0000-000000000000"), "new@mail.ru", "1234"));

        assertThatThrownBy(() -> userService.getUserByEmail("ex@mail.ru")).isInstanceOf(UserNotFoundException.class);

        var user = userService.getUserByEmail("new@mail.ru");
        assertThat(user.getId()).isEqualTo(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        assertThat(user.getEmail()).isEqualTo("new@mail.ru");
        assertThat(user.getPassword()).isEqualTo(PasswordManager.getPasswordHash("1234"));
    }

    @DisplayName("Check that checkEmailExist return expected result witch depends on email exist")
    @ParameterizedTest
    @CsvSource({"ex@mail.ru,true", "notExitedEmail@mail.ru,false"})
    void checkEmailExist_shouldReturnTrue_whenEmailExist_ElseFalse(String email, String expectedBoolStr) {
        var expectedBool = Boolean.parseBoolean(expectedBoolStr);
        assertThat(userService.checkEmailExist(email)).isEqualTo(expectedBool);
    }

    @DisplayName("Check remove user working is correct")
    @Test
    void remove_shouldRemoveUserAndTheirHabitsAndTracks() {
        userService.remove(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        assertThatThrownBy(() -> userService.getUserByEmail("ex@mail.ru")).isInstanceOf(UserNotFoundException.class);
    }

    @DisplayName("Check that changeUserAdminStatus change admin to true if provide true")
    @Test
    void changeUserAdminStatus_shouldChangeStatusToTrue_whenProvideTrue() throws UserNotFoundException {
        userService.changeUserAdminStatus(new ChangeAdminStatusDto(UUID.fromString("00000000-0000-0000-0000-000000000000"), true));
        var user = userService.getUserByEmail("ex@mail.ru");
        assertThat(user.isAdmin()).isTrue();
    }

    @DisplayName("Check that changeUserAdminStatus change admin to false if provide false")
    @Test
    void changeUserAdminStatus_shouldChangeStatusToFalse_whenProvideFalse() throws UserNotFoundException {
        userService.changeUserAdminStatus(new ChangeAdminStatusDto(UUID.fromString("00000000-0000-0000-0000-000000000001"), false));
        var user = userService.getUserByEmail("admin");
        assertThat(user.isAdmin()).isFalse();
    }
}