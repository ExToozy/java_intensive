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
 * �������� ��������, ����������� ��������������� ����� ���������� �������������.
 * ������������ �������� �������������.
 */
public class ActionManager {
    private final ConsoleUserController userController;
    private final ConsoleAuthController authController;
    private final ConsoleHabitController habitController;
    private final ConsoleHabitTrackController habitTrackController;

    /**
     * ����������� {@link ActionManager}.
     *
     * @param userController       ���������� ��� ������ � ��������������
     * @param authController       ���������� ��� ������ � ���������������
     * @param habitController      ���������� ��� ������ � ����������
     * @param habitTrackController ���������� ��� ������ � ��������� ��������
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
     * ��������� ���������� �������������� ��� �������������.
     *
     * @return ������������������� ������������
     * @throws UserNotFoundException     ���� ������������ �� ������
     * @throws UserAlreadyExistException ���� ������������ ��� ����������
     * @throws InvalidEmailException     ���� email ��������������
     */
    public User manageAuthAction() throws UserNotFoundException, UserAlreadyExistException, InvalidEmailException {
        AuthActionHandler handler = new AuthActionHandler(authController);
        AuthAction action = ConsoleInHelper.getActionFromInput(AuthAction.class);
        return handler.handleAuthUserAction(action);
    }

    /**
     * ��������� ���������� ��������������.
     *
     * @param user ������������, ����������� �������� ��������������
     * @return ���� ������ �� ��������� ��������
     */
    public boolean manageAdminAction(User user) {
        boolean exitFlag;
        AdminActionHandler handler = new AdminActionHandler(userController, this);
        AdminAction action = ConsoleInHelper.getActionFromInput(AdminAction.class);
        exitFlag = handler.handleAdminAction(user, action);
        return exitFlag;
    }

    /**
     * ��������� ���������� �������������� �� ��������� � �������������.
     *
     * @param openedUser  �������� ������������
     * @param currentUser ������� ������������(�������������)
     * @return ���� ������ �� ��������� ��������
     */
    public boolean manageAdminActionOnUser(User openedUser, User currentUser) {
        AdminActionOnUserHandler handler = new AdminActionOnUserHandler(userController, this);
        AdminActionOnUser action = ConsoleInHelper.getActionFromInput(AdminActionOnUser.class);
        return handler.handleAdminActionOnUser(openedUser, action, currentUser);
    }

    /**
     * ��������� ���������� �������������.
     *
     * @param user ������������, ����������� ��������
     * @return ���� ������ �� ��������� ��������
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
     * ��������� ���������� ��� ����������� ������������.
     *
     * @param user ������������ ��� ����������
     * @return ���� ��������� ����������
     */
    public boolean manageUpdateUserAction(User user) {
        UpdateUserHandler handler = new UpdateUserHandler(userController);
        UpdateUserAction action = ConsoleInHelper.getActionFromInput(UpdateUserAction.class);
        return handler.handleUpdateUserAction(action, user);
    }

    /**
     * ��������� ���������� � ���������.
     *
     * @param habit �������� ��� ���������� ��������
     */
    public void manageHabitAction(Habit habit) {
        HabitActionHandler handler = new HabitActionHandler(habitTrackController, habitController, this);
        int habitStreak = habitController.getHabitStreak(habit);
        ConsoleOutHelper.printHabitInfo(habit, habitStreak);
        HabitAction action = ConsoleInHelper.getActionFromInput(HabitAction.class);
        handler.handleHabitAction(action, habit);
    }

    /**
     * ��������� ���������� ��� ����������� ��������.
     *
     * @param habit �������� ��� ����������
     */
    public void manageUpdateHabitAction(Habit habit) {
        UpdateHabitActionHandler handler = new UpdateHabitActionHandler(habitController);
        handler.handleUpdateHabitAction(habit);
    }
}
