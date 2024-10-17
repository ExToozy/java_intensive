package org.example.presentation.console.handlers;

import org.example.core.models.User;
import org.example.infastructure.controllers.console.ConsoleUserController;
import org.example.presentation.console.ActionManager;
import org.example.presentation.console.actions.AdminAction;
import org.example.presentation.console.in.ConsoleInHelper;
import org.example.presentation.console.out.ConsoleOutHelper;

import java.util.List;

/**
 * ���������� �������� ��������������.
 */
public class AdminActionHandler {
    private final ConsoleUserController userController;
    private final ActionManager actionManager;

    /**
     * ����������� AdminActionHandler.
     *
     * @param userController ���������� ��� ������ � ��������������
     * @param actionManager  �������� ��������
     */
    public AdminActionHandler(ConsoleUserController userController, ActionManager actionManager) {
        this.userController = userController;
        this.actionManager = actionManager;
    }

    /**
     * ������������ �������� ��������������.
     *
     * @param user   �������������, ����������� ��������
     * @param action �������� ��������������
     * @return ���� ������
     */
    public boolean handleAdminAction(User user, AdminAction action) {
        switch (action) {
            case OPEN_HABIT_TRACKER:
                boolean exitFlag = false;
                while (!exitFlag) {
                    exitFlag = actionManager.manageUserAction(user);
                }
                break;
            case SHOW_USERS:
                List<User> users = userController.getAll();
                ConsoleOutHelper.printUsers(users);
                int userIndex = ConsoleInHelper.getUserIndexFromInput(users);
                return actionManager.manageAdminActionOnUser(users.get(userIndex), user);
            case EXIT:
                break;
        }
        return false;
    }
}
