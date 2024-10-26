package org.example.presentation.console;

import org.example.infrastructure.controllers.console.ConsoleAuthController;
import org.example.infrastructure.controllers.console.ConsoleHabitController;
import org.example.infrastructure.controllers.console.ConsoleHabitTrackController;
import org.example.infrastructure.controllers.console.ConsoleUserController;
import org.example.infrastructure.util.ServiceHelper;

public class HabitTrackerRunner {
    public void run() {
        try {
            ConsoleUserController userController = new ConsoleUserController(ServiceHelper.getUserServiceInstance());
            ConsoleAuthController authController = new ConsoleAuthController(ServiceHelper.getAuthServiceInstance());
            ConsoleHabitController habitController = new ConsoleHabitController(ServiceHelper.getHabitServiceInstance());
            ConsoleHabitTrackController habitTrackController = new ConsoleHabitTrackController(ServiceHelper.getHabitTrackServiceInstance());

            HabitTracker habitTracker = new HabitTracker(userController, authController, habitController, habitTrackController);

            habitTracker.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
