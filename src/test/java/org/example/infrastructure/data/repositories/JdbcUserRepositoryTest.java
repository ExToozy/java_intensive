package org.example.infrastructure.data.repositories;

import org.assertj.core.api.SoftAssertions;
import org.example.core.dtos.user_dtos.AuthUserDto;
import org.example.core.dtos.user_dtos.ChangeAdminStatusDto;
import org.example.core.dtos.user_dtos.UpdateUserDto;
import org.example.core.util.PasswordManager;
import org.example.exceptions.UserNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class JdbcUserRepositoryTest {

    @Autowired
    private JdbcUserRepository userRepository;

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