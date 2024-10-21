package org.example.presentation.console;

import org.example.core.repositories.habit_repository.IHabitRepository;
import org.example.core.repositories.habit_track_repository.IHabitTrackRepository;
import org.example.core.repositories.user_repository.IUserRepository;
import org.example.core.services.AuthService;
import org.example.core.services.HabitService;
import org.example.core.services.HabitTrackService;
import org.example.core.services.UserService;
import org.example.infrastructure.controllers.console.ConsoleAuthController;
import org.example.infrastructure.controllers.console.ConsoleHabitController;
import org.example.infrastructure.controllers.console.ConsoleHabitTrackController;
import org.example.infrastructure.controllers.console.ConsoleUserController;
import org.example.infrastructure.data.repositories.jdbc_repositories.JdbcHabitRepository;
import org.example.infrastructure.data.repositories.jdbc_repositories.JdbcHabitTrackRepository;
import org.example.infrastructure.data.repositories.jdbc_repositories.JdbcUserRepository;

public class HabitTrackerRunner {
    public void run() {
        try {
            IUserRepository userRepository = new JdbcUserRepository();
            IHabitRepository habitRepository = new JdbcHabitRepository();
            IHabitTrackRepository habitTrackRepository = new JdbcHabitTrackRepository();

            HabitTrackService habitTrackService = new HabitTrackService(habitTrackRepository);
            HabitService habitService = new HabitService(habitRepository, habitTrackService);
            UserService userService = new UserService(userRepository, habitService);
            AuthService authService = new AuthService(userService);

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
