package org.example.presentation.console;

import org.example.core.exceptions.InvalidEmailException;
import org.example.core.exceptions.UserAlreadyExistException;
import org.example.core.exceptions.UserNotFoundException;
import org.example.core.models.Habit;
import org.example.core.models.User;
import org.example.infastructure.controllers.console.ConsoleAuthController;
import org.example.infastructure.controllers.console.ConsoleHabitController;
import org.example.infastructure.controllers.console.ConsoleHabitTrackController;
import org.example.infastructure.controllers.console.ConsoleUserController;
import org.example.presentation.console.actions.*;
import org.example.presentation.console.handlers.*;
import org.example.presentation.console.in.ConsoleInHelper;
import org.example.presentation.console.out.ConsoleOutHelper;

/**
 * ћенеджер действий, управл€ющий взаимодействием между различными контроллерами.
 * ќбрабатывает действи€ пользователей.
 */
public class ActionManager {
    private final ConsoleUserController userController;
    private final ConsoleAuthController authController;
    private final ConsoleHabitController habitController;
    private final ConsoleHabitTrackController habitTrackController;

    /**
     *  онструктор {@link ActionManager}.
     *
     * @param userController       контроллер дл€ работы с пользовател€ми
     * @param authController       контроллер дл€ работы с аутентификацией
     * @param habitController      контроллер дл€ работы с привычками
     * @param habitTrackController контроллер дл€ работы с отметками привычки
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
     * ”правл€ет действи€ми аутентификации дл€ пользователей.
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
     * ”правл€ет действи€ми администратора.
     *
     * @param user пользователь, выполн€ющий действи€ администратора
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
     * ”правл€ет действи€ми администратора по отношению к пользовател€м.
     *
     * @param openedUser  открытый пользователь
     * @param currentUser текущий пользователь(јдминистратор)
     * @return флаг выхода из менеджера действий
     */
    public boolean manageAdminActionOnUser(User openedUser, User currentUser) {
        AdminActionOnUserHandler handler = new AdminActionOnUserHandler(userController, this);
        AdminActionOnUser action = ConsoleInHelper.getActionFromInput(AdminActionOnUser.class);
        return handler.handleAdminActionOnUser(openedUser, action, currentUser);
    }

    /**
     * ”правл€ет действи€ми пользователей.
     *
     * @param user пользователь, выполн€ющий действи€
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
     * ”правл€ет действи€ми дл€ обновлением пользовател€.
     *
     * @param user пользователь дл€ обновлени€
     * @return флаг успешного обновлени€
     */
    public boolean manageUpdateUserAction(User user) {
        UpdateUserHandler handler = new UpdateUserHandler(userController);
        UpdateUserAction action = ConsoleInHelper.getActionFromInput(UpdateUserAction.class);
        return handler.handleUpdateUserAction(action, user);
    }

    /**
     * ”правл€ет действи€ми с привычкой.
     *
     * @param habit привычка дл€ выполнени€ действи€
     */
    public void manageHabitAction(Habit habit) {
        HabitActionHandler handler = new HabitActionHandler(habitTrackController, habitController, this);
        int habitStreak = habitController.getHabitStreak(habit);
        ConsoleOutHelper.printHabitInfo(habit, habitStreak);
        HabitAction action = ConsoleInHelper.getActionFromInput(HabitAction.class);
        handler.handleHabitAction(action, habit);
    }

    /**
     * ”правл€ет действи€ми дл€ обновлением привычки.
     *
     * @param habit привычка дл€ обновлени€
     */
    public void manageUpdateHabitAction(Habit habit) {
        UpdateHabitActionHandler handler = new UpdateHabitActionHandler(habitController);
        handler.handleUpdateHabitAction(habit);
    }
}
