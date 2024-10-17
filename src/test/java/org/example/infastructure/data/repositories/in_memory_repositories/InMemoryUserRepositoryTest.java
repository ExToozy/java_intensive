package org.example.infastructure.data.repositories.in_memory_repositories;

import org.example.core.exceptions.UserNotFoundException;
import org.example.core.repositories.user_repository.dtos.ChangeAdminStatusDto;
import org.example.core.repositories.user_repository.dtos.CreateUserDto;
import org.example.core.repositories.user_repository.dtos.UpdateUserDto;
import org.example.core.util.PasswordManager;
import org.example.infastructure.data.models.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InMemoryUserRepositoryTest {
    InMemoryUserRepository userRepository;

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
        userRepository = new InMemoryUserRepository();
        Field field = InMemoryUserRepository.class.getDeclaredField("users");
        field.setAccessible(true);
        field.set(userRepository, users);
    }

    @DisplayName("Check create working correctly")
    @Test
    void create_shouldCreateUserInMemory() throws UserNotFoundException {
        userRepository.create(new CreateUserDto("test@mail.ru", "123"));

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
                .hasSize(2)
                .allMatch(user -> user.getId() != null && user.getEmail() != null && user.getPassword() != null);
    }

    @DisplayName("Check update working correctly")
    @Test
    void update_shouldUpdateUser_whenUserExist() throws UserNotFoundException {
        userRepository.update(new UpdateUserDto(UUID.fromString("00000000-0000-0000-0000-000000000000"), "new@mail.ru", "1234"));

        assertThatThrownBy(() -> userRepository.getByEmail("ex@mail.ru")).isInstanceOf(UserNotFoundException.class);

        var user = userRepository.getByEmail("new@mail.ru");
        assertThat(user.getId()).isEqualTo(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        assertThat(user.getEmail()).isEqualTo("new@mail.ru");
        assertThat(user.getPassword()).isEqualTo(PasswordManager.getPasswordHash("1234"));
    }

    @DisplayName("Check remove working correctly")
    @Test
    void remove_shouldRemove_whenUserExist() {
        userRepository.remove(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        assertThatThrownBy(() -> userRepository.getByEmail("ex@mail.ru")).isInstanceOf(UserNotFoundException.class);
    }

    @DisplayName("Check changeUserAdminStatus to true")
    @Test
    void changeUserAdminStatus_shouldChangeToTrue_whenProvideTrue() throws UserNotFoundException {
        userRepository.changeUserAdminStatus(new ChangeAdminStatusDto(UUID.fromString("00000000-0000-0000-0000-000000000000"), true));
        var user = userRepository.getByEmail("ex@mail.ru");
        assertThat(user.isAdmin()).isTrue();
    }

    @DisplayName("Check changeUserAdminStatus to false")
    @Test
    void changeUserAdminStatus_shouldChangeToFalse_whenProvideFalse() throws UserNotFoundException {
        userRepository.changeUserAdminStatus(new ChangeAdminStatusDto(UUID.fromString("00000000-0000-0000-0000-000000000001"), false));
        var user = userRepository.getByEmail("admin");
        assertThat(user.isAdmin()).isFalse();
    }
}