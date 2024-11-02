package org.example.presentation.console;

import org.example.core.exceptions.InvalidEmailException;
import org.example.core.exceptions.UserAlreadyExistException;
import org.example.core.exceptions.UserNotFoundException;
import org.example.core.models.Habit;
import org.example.core.models.User;
import org.example.infrastructure.controllers.console.ConsoleAuthController;
import org.example.infrastructure.controllers.console.ConsoleHabitController;
import org.example.infrastructure.controllers.console.ConsoleHabitTrackController;
import org.example.infrastructure.controllers.console.ConsoleUserController;
import org.example.presentation.console.actions.AdminAction;
import org.example.presentation.console.actions.AdminActionOnUser;
import org.example.presentation.console.actions.AuthAction;
import org.example.presentation.console.actions.HabitAction;
import org.example.presentation.console.actions.UpdateUserAction;
import org.example.presentation.console.actions.UserAction;
import org.example.presentation.console.handlers.AdminActionHandler;
import org.example.presentation.console.handlers.AdminActionOnUserHandler;
import org.example.presentation.console.handlers.AuthActionHandler;
import org.example.presentation.console.handlers.HabitActionHandler;
import org.example.presentation.console.handlers.UpdateHabitActionHandler;
import org.example.presentation.console.handlers.UpdateUserHandler;
import org.example.presentation.console.handlers.UserActionHandler;
import org.example.presentation.console.in.ConsoleInHelper;
import org.example.presentation.console.out.ConsoleOutHelper;


/**
 * Менеджер действий, управляющий взаимодействием между различными контроллерами.
 * Обрабатывает действия пользователей.
 */
public class ActionManager {
    private final ConsoleUserController userController;
    private final ConsoleAuthController authController;
    private final ConsoleHabitController habitController;
    private final ConsoleHabitTrackController habitTrackController;

    /**
     * Конструктор {@link ActionManager}.
     *
     * @param userController       контроллер для работы с пользователями
     * @param authController       контроллер для работы с аутентификацией
     * @param habitController      контроллер для работы с привычками
     * @param habitTrackController контроллер для работы с отметками привычки
     */
    public ActionManager(
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

    /**
     * Управляет действиями аутентификации для пользователей.
     *
     * @return аутентифицированный пользователь
     * @throws UserNotFoundException     если пользователь не найден
     * @throws UserAlreadyExistException если пользователь уже существует
     * @throws InvalidEmailException     если email недействителен
     */
    public User manageAuthAction() throws UserNotFoundException, UserAlreadyExistException, InvalidEmailException {
        AuthActionHandler handler = new AuthActionHandler(authController);
        AuthAction action = ConsoleInHelper.getActionFromInput(AuthAction.class);
        return handler.handleAuthUserAction(action);
    }

    /**
     * Управляет действиями администратора.
     *
     * @param user пользователь, выполняющий действия администратора
     * @return флаг выхода из менеджера действий
     */
    public boolean manageAdminAction(User user) {
        boolean exitFlag;
        AdminActionHandler handler = new AdminActionHandler(userController, this);
        AdminAction action = ConsoleInHelper.getActionFromInput(AdminAction.class);
        exitFlag = handler.handleAdminAction(user, action);
        return exitFlag;
    }

    /**
     * Управляет действиями администратора по отношению к пользователям.
     *
     * @param openedUser  открытый пользователь
     * @param currentUser текущий пользователь(Администратор)
     * @return флаг выхода из менеджера действий
     */
    public boolean manageAdminActionOnUser(User openedUser, User currentUser) {
        AdminActionOnUserHandler handler = new AdminActionOnUserHandler(userController, this);
        AdminActionOnUser action = ConsoleInHelper.getActionFromInput(AdminActionOnUser.class);
        return handler.handleAdminActionOnUser(openedUser, action, currentUser);
    }

    /**
     * Управляет действиями пользователей.
     *
     * @param user пользователь, выполняющий действия
     * @return флаг выхода из менеджера действий
     */
    public boolean manageUserAction(User user) {
        UserActionHandler handler = new UserActionHandler(
                habitController,
                this,
                userController,
                habitTrackController
        );
        UserAction userAction = ConsoleInHelper.getActionFromInput(UserAction.class);
        return handler.handleUserAction(user, userAction);
    }

    /**
     * Управляет действиями для обновлением пользователя.
     *
     * @param user пользователь для обновления
     * @return флаг успешного обновления
     */
    public boolean manageUpdateUserAction(User user) {
        UpdateUserHandler handler = new UpdateUserHandler(userController);
        UpdateUserAction action = ConsoleInHelper.getActionFromInput(UpdateUserAction.class);
        return handler.handleUpdateUserAction(action, user);
    }

    /**
     * Управляет действиями с привычкой.
     *
     * @param habit привычка для выполнения действия
     */
    public void manageHabitAction(Habit habit) {
        HabitActionHandler handler = new HabitActionHandler(habitTrackController, habitController, this);
        int habitStreak = habitController.getHabitStreak(habit);
        ConsoleOutHelper.printHabitInfo(habit, habitStreak);
        HabitAction action = ConsoleInHelper.getActionFromInput(HabitAction.class);
        handler.handleHabitAction(action, habit);
    }

    /**
     * Управляет действиями для обновлением привычки.
     *
     * @param habit привычка для обновления
     */
    public void manageUpdateHabitAction(Habit habit) {
        UpdateHabitActionHandler handler = new UpdateHabitActionHandler(habitController);
        handler.handleUpdateHabitAction(habit);
    }
}
