package org.example.presentation;

import org.example.core.exceptions.UserNotFoundException;
import org.example.core.models.User;
import org.example.core.repositories.habit_repository.IHabitRepository;
import org.example.core.repositories.habit_track_repository.IHabitTrackRepository;
import org.example.core.repositories.user_repository.IUserRepository;
import org.example.core.services.AuthService;
import org.example.core.services.HabitService;
import org.example.core.services.HabitTrackService;
import org.example.core.services.UserService;
import org.example.infastructure.controllers.console.ConsoleAuthController;
import org.example.infastructure.controllers.console.ConsoleHabitController;
import org.example.infastructure.controllers.console.ConsoleHabitTrackController;
import org.example.infastructure.controllers.console.ConsoleUserController;
import org.example.infastructure.data.repositories.in_memory_repositories.InMemoryHabitRepository;
import org.example.infastructure.data.repositories.in_memory_repositories.InMemoryHabitTrackRepository;
import org.example.infastructure.data.repositories.in_memory_repositories.InMemoryUserRepository;
import org.example.presentation.console.HabitTracker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

class HabitTrackerIntegrationTest {
    HabitTracker habitTracker;
    InputStream systemIn = System.in;
    ByteArrayInputStream testIn;
    IUserRepository userRepository;
    IHabitRepository habitRepository;
    IHabitTrackRepository habitTrackRepository;

    @AfterEach
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
    }

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        habitRepository = new InMemoryHabitRepository();
        habitTrackRepository = new InMemoryHabitTrackRepository();

        HabitTrackService habitTrackService = new HabitTrackService(habitTrackRepository);
        HabitService habitService = new HabitService(habitRepository, habitTrackService);
        UserService userService = new UserService(userRepository, habitService);
        AuthService authService = new AuthService(userService);

        ConsoleUserController userController = new ConsoleUserController(userService);
        ConsoleAuthController authController = new ConsoleAuthController(authService);
        ConsoleHabitController habitController = new ConsoleHabitController(habitService);
        ConsoleHabitTrackController habitTrackController = new ConsoleHabitTrackController(habitTrackService);

        habitTracker = new HabitTracker(userController, authController, habitController, habitTrackController);
    }

    @DisplayName("Check that habitTracker can create habit after auth user ")
    @Test
    void run_shouldCreateHabit() throws UserNotFoundException {
        String inputData = """
                1
                ex@mail.ru
                123
                1
                testCreatingHabit
                testHabitDescription
                1
                6
                n""";
        provideInput(inputData);
        habitTracker.run();
        User user = userRepository.getByEmail("ex@mail.ru");
        var habits = habitRepository.getAllHabitsByUserId(user.getId());
        var createdHabit = habits
                .stream()
                .filter(habit -> habit.getName().equals("testCreatingHabit"))
                .findFirst()
                .orElse(null);
        assertThat(createdHabit).isNotNull();
    }

    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }
}