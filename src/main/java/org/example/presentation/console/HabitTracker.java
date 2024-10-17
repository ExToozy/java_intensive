package org.example.presentation.console;

import org.example.core.exceptions.InvalidEmailException;
import org.example.core.exceptions.UserAlreadyExistException;
import org.example.core.exceptions.UserNotFoundException;
import org.example.core.models.User;
import org.example.infastructure.controllers.console.ConsoleAuthController;
import org.example.infastructure.controllers.console.ConsoleHabitController;
import org.example.infastructure.controllers.console.ConsoleHabitTrackController;
import org.example.infastructure.controllers.console.ConsoleUserController;
import org.example.presentation.console.in.ConsoleInHelper;
import org.example.presentation.console.out.ConsoleOutHelper;

public class HabitTracker {
    private final ConsoleUserController userController;
    private final ConsoleAuthController authController;
    private final ConsoleHabitController habitController;
    private final ConsoleHabitTrackController habitTrackController;

    public HabitTracker(
            ConsoleUserController userController,
            ConsoleAuthController authController,
            ConsoleHabitController habitController,
            ConsoleHabitTrackController habitTrackController
    ) {
        this.userController = userController;
        this.authController = authController;
        this.habitController = habitController;
        this.habitTrackController = habitTrackController;
    }

    public void run() {
        ActionManager actionManager = new ActionManager(
                userController,
                authController,
                habitController,
                habitTrackController
        );
        do {
            User user = runAuthLoop(actionManager);
            if (user == null) {
                continue;
            }
            runMainLoop(user, actionManager);

        } while (ConsoleInHelper.getExitAnswer());
        ConsoleOutHelper.printMessage("Thank you for being with us");
    }

    private User runAuthLoop(ActionManager actionManager) {
        User user = null;
        try {
            user = actionManager.manageAuthAction();
        } catch (UserNotFoundException e) {
            ConsoleOutHelper.printMessage("User with these credentials not found");
        } catch (UserAlreadyExistException e) {
            ConsoleOutHelper.printMessage("A user with such emails already exists");
        } catch (InvalidEmailException e) {
            ConsoleOutHelper.printMessage("Please enter a valid email");
        }
        return user;
    }

    private void runMainLoop(User user, ActionManager actionManager) {
        boolean exitFlag = false;
        while (!exitFlag) {
            if (user.isAdmin()) {
                exitFlag = actionManager.manageAdminAction(user);
            } else {
                exitFlag = actionManager.manageUserAction(user);
            }
        }
    }
}
