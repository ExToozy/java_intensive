package org.example.presentation.console.handlers;

import org.example.core.exceptions.UserNotFoundException;
import org.example.core.models.User;
import org.example.core.repositories.user_repository.dtos.ChangeAdminStatusDto;
import org.example.infastructure.controllers.console.ConsoleUserController;
import org.example.presentation.console.ActionManager;
import org.example.presentation.console.actions.AdminActionOnUser;
import org.example.presentation.console.out.ConsoleOutHelper;

/**
 * ���������� �������� �������������� � ��������� ������������.
 * ������������ ��������, ����� ��� �������� �������� ������������, ��������� ������� ������������ � �������� ������������.
 */
public class AdminActionOnUserHandler {
    private final ConsoleUserController userController;
    private final ActionManager actionManager;

    /**
     * ����������� AdminActionOnUserHandler.
     *
     * @param userController ���������� ��� ������ � ��������������
     * @param actionManager  �������� ��������
     */
    public AdminActionOnUserHandler(ConsoleUserController userController, ActionManager actionManager) {
        this.userController = userController;
        this.actionManager = actionManager;
    }

    /**
     * ������������ �������� �������������� � ��������� ������������.
     *
     * @param openedUser  ������������, � �������� ����������� ��������
     * @param action      �������� ��������������
     * @param currentUser ������� ������������(�������������), ����������� ��������
     * @return true, ���� ��������� �����, ����� false
     */
    public boolean handleAdminActionOnUser(User openedUser, AdminActionOnUser action, User currentUser) {
        switch (action) {
            case OPEN_HABITS -> actionManager.manageUserAction(openedUser);
            case CHANGE_ADMIN_STATUS -> {
                try {
                    userController.changeUserAdminStatus(new ChangeAdminStatusDto(openedUser.getId(), !openedUser.isAdmin()));
                    return openedUser.getId().equals(currentUser.getId());
                } catch (UserNotFoundException e) {
                    ConsoleOutHelper.printMessage("An error occurred while changing the status. The user may have already been deleted");
                }
            }
            case DELETE -> {
                userController.deleteUser(openedUser.getId());
                return openedUser.getId().equals(currentUser.getId());
            }
            case EXIT -> {
                return true;
            }
        }
        return false;
    }
}
