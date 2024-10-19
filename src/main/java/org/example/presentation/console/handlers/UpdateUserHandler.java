package org.example.presentation.console.handlers;

import org.example.core.exceptions.InvalidEmailException;
import org.example.core.exceptions.UserNotFoundException;
import org.example.core.models.User;
import org.example.core.repositories.user_repository.dtos.UpdateUserDto;
import org.example.infrastructure.controllers.console.ConsoleUserController;
import org.example.presentation.console.actions.UpdateUserAction;
import org.example.presentation.console.in.ConsoleInHelper;
import org.example.presentation.console.out.ConsoleOutHelper;

/**
 * Обработчик действий обновления пользователя.
 * Обрабатывает обновление информации о пользователе.
 */
public class UpdateUserHandler {
    private final ConsoleUserController userController;

    /**
     * Конструктор UpdateUserHandler.
     *
     * @param userController контроллер для работы с пользователями
     */
    public UpdateUserHandler(ConsoleUserController userController) {
        this.userController = userController;
    }

    /**
     * Обрабатывает действия обновления пользователя.
     *
     * @param action действие обновления пользователя
     * @param user   пользователь для обновления
     * @return флаг успешного обновления
     */
    public boolean handleUpdateUserAction(UpdateUserAction action, User user) {
        try {
            switch (action) {
                case UPDATE_EMAIL:
                    handleUpdateUserEmailAction(user);
                    break;
                case UPDATE_PASSWORD:
                    handleUpdateUserPasswordAction(user);
                    break;
                case CANCEL:
                    break;
            }
            return true;
        } catch (UserNotFoundException e) {
            ConsoleOutHelper.printMessage("Error while updating, try later");
        } catch (InvalidEmailException e) {
            ConsoleOutHelper.printMessage("Invalid email. Try again");
        }
        return false;
    }

    /**
     * Обрабатывает обновление email пользователя.
     *
     * @param user пользователь для обновления
     * @throws UserNotFoundException если пользователь не найден
     * @throws InvalidEmailException если пользователь ввёл некорректный email
     */
    private void handleUpdateUserEmailAction(User user) throws UserNotFoundException, InvalidEmailException {
        String email = ConsoleOutHelper.getUserEmailFromInput();
        userController.updateUser(new UpdateUserDto(user.getId(), email, user.getEmail()));
    }

    /**
     * Обрабатывает обновление пароля пользователя.
     *
     * @param user пользователь для обновления
     * @throws UserNotFoundException если пользователь не найден
     * @throws InvalidEmailException если пользователь ввёл некорректный email
     */
    private void handleUpdateUserPasswordAction(User user) throws UserNotFoundException, InvalidEmailException {
        String password = ConsoleInHelper.getUserPasswordFromInput();
        userController.updateUser(new UpdateUserDto(user.getId(), user.getEmail(), password));
    }
}
