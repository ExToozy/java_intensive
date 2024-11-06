package org.example.core.services;

import org.assertj.core.api.SoftAssertions;
import org.example.core.dtos.user_dtos.AuthUserDto;
import org.example.core.dtos.user_dtos.ChangeAdminStatusDto;
import org.example.core.dtos.user_dtos.UpdateUserDto;
import org.example.core.util.PasswordManager;
import org.example.exceptions.InvalidEmailException;
import org.example.exceptions.UserNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
class UserServiceTest {

    @Autowired
    UserService userService;

    @DisplayName("Check that create successfully create user")
    @Test
    void create_shouldCreateUser() throws UserNotFoundException {
        userService.create(new AuthUserDto("test@mail.ru", "123"));

        var user = userService.getUserByEmail("test@mail.ru");

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(user).isNotNull();
        softly.assertThat(user.getPassword()).isEqualTo("123");
        softly.assertThat(user.getId()).isNotNull();
        softly.assertAll();
    }

    @DisplayName("Check getUserByEmail return user by email correctly")
    @Test
    void getUserByEmail_shouldGetCorrectUser() throws UserNotFoundException {
        var user = userService.getUserByEmail("ex@mail.ru");

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(user).isNotNull();
        softly.assertThat(user.getPassword()).isEqualTo(PasswordManager.getPasswordHash("123"));
        softly.assertThat(user.getId()).isNotNull();
        softly.assertAll();
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
                .hasSize(3)
                .allMatch(user -> user.getEmail() != null && user.getPassword() != null);
    }

    @DisplayName("Check update user working is correct")
    @Test
    void update_shouldUpdateUser_whenEmailIsCorrectAndUserExist() throws UserNotFoundException, InvalidEmailException {
        userService.update(1, new UpdateUserDto("new@mail.ru", "1234"));

        assertThatThrownBy(() -> userService.getUserByEmail("ex@mail.ru")).isInstanceOf(UserNotFoundException.class);

        var user = userService.getUserByEmail("new@mail.ru");

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(user.getId()).isEqualTo(1);
        softly.assertThat(user.getEmail()).isEqualTo("new@mail.ru");
        softly.assertThat(user.getPassword()).isEqualTo(PasswordManager.getPasswordHash("1234"));
        softly.assertAll();
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
        userService.remove(1);
        assertThatThrownBy(() -> userService.getUserByEmail("ex@mail.ru")).isInstanceOf(UserNotFoundException.class);
    }

    @DisplayName("Check that changeUserAdminStatus change admin to true if provide true")
    @Test
    void changeUserAdminStatus_shouldChangeStatusToTrue_whenProvideTrue() throws UserNotFoundException {
        userService.changeUserAdminStatus(1, new ChangeAdminStatusDto(true));
        var user = userService.getUserByEmail("ex@mail.ru");
        assertThat(user.isAdmin()).isTrue();
    }

    @DisplayName("Check that changeUserAdminStatus change admin to false if provide false")
    @Test
    void changeUserAdminStatus_shouldChangeStatusToFalse_whenProvideFalse() throws UserNotFoundException {
        userService.changeUserAdminStatus(2, new ChangeAdminStatusDto(false));
        var user = userService.getUserByEmail("admin");
        assertThat(user.isAdmin()).isFalse();
    }
}