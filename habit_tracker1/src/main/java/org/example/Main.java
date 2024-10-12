package org.example;

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
import org.example.presentation.HabitTracker;

public class Main {
    public static void main(String[] args) {
        try {
            IUserRepository userRepository = new InMemoryUserRepository();
            IHabitRepository habitRepository = new InMemoryHabitRepository();
            IHabitTrackRepository habitTrackRepository = new InMemoryHabitTrackRepository();

            UserService userService = new UserService(userRepository);
            AuthService authService = new AuthService(userService);
            HabitTrackService habitTrackService = new HabitTrackService(habitTrackRepository);
            HabitService habitService = new HabitService(habitRepository, habitTrackService);

            ConsoleUserController userController = new ConsoleUserController(userService);
            ConsoleAuthController authController = new ConsoleAuthController(authService);
            ConsoleHabitController habitController = new ConsoleHabitController(habitService);
            ConsoleHabitTrackController habitTrackController = new ConsoleHabitTrackController(habitTrackService);

            HabitTracker habitTracker = new HabitTracker(userController, authController, habitController, habitTrackController);

            habitTracker.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}