package org.example.presentation.console.handlers;

import org.example.core.dtos.user_dtos.ChangeAdminStatusDto;
import org.example.core.exceptions.UserNotFoundException;
import org.example.core.models.User;
import org.example.infrastructure.controllers.console.ConsoleUserController;
import org.example.presentation.console.ActionManager;
import org.example.presentation.console.actions.AdminActionOnUser;
import org.example.presentation.console.out.ConsoleOutHelper;

/**
 * Обработчик действий администратора в отношении пользователя.
 * Обрабатывает действия, такие как открытие привычек пользователя, изменение статуса пользователя и удаление пользователя.
 */
public class AdminActionOnUserHandler {
    private final ConsoleUserController userController;
    private final ActionManager actionManager;

    /**
     * Конструктор AdminActionOnUserHandler.
     *
     * @param userController контроллер для работы с пользователями
     * @param actionManager  менеджер действий
     */
    public AdminActionOnUserHandler(ConsoleUserController userController, ActionManager actionManager) {
        this.userController = userController;
        this.actionManager = actionManager;
    }

    /**
     * Обрабатывает действия администратора в отношении пользователя.
     *
     * @param openedUser  пользователь, к которому применяется действие
     * @param action      действие администратора
     * @param currentUser текущий пользователь(администратор), выполняющий действие
     * @return true, если требуется выход, иначе false
     */
    public boolean handleAdminActionOnUser(User openedUser, AdminActionOnUser action, User currentUser) {
        switch (action) {
            case OPEN_HABITS -> actionManager.manageUserAction(openedUser);
            case CHANGE_ADMIN_STATUS -> {
                try {
                    userController.changeUserAdminStatus(new ChangeAdminStatusDto(openedUser.getId(), !openedUser.isAdmin()));
                    return openedUser.getId() == currentUser.getId();
                } catch (UserNotFoundException e) {
                    ConsoleOutHelper.printMessage("An error occurred while changing the status. The user may have already been deleted");
                }
            }
            case DELETE -> {
                userController.deleteUser(openedUser.getId());
                return openedUser.getId() == currentUser.getId();
            }
            case EXIT -> {
                return true;
            }
        }
        return false;
    }
}
