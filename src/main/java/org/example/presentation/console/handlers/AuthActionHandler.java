package org.example.presentation.console.handlers;

import org.example.core.exceptions.InvalidEmailException;
import org.example.core.exceptions.UserAlreadyExistException;
import org.example.core.exceptions.UserNotFoundException;
import org.example.core.models.User;
import org.example.core.repositories.user_repository.dtos.CreateUserDto;
import org.example.infastructure.controllers.console.ConsoleAuthController;
import org.example.presentation.console.actions.AuthAction;
import org.example.presentation.console.in.ConsoleInHelper;

/**
 * Обработчик действий аутентификации.
 * Обрабатывает действия входа и регистрации пользователей.
 */
public class AuthActionHandler {
    private final ConsoleAuthController authController;

    /**
     * Конструктор AuthActionHandler.
     *
     * @param authController контроллер для работы с аутентификацией
     */
    public AuthActionHandler(ConsoleAuthController authController) {
        this.authController = authController;
    }

    /**
     * Обрабатывает действия аутентификации пользователя.
     *
     * @param action действие аутентификации
     * @return аутентифицированный пользователь
     * @throws UserNotFoundException     если пользователь не найден
     * @throws UserAlreadyExistException если пользователь уже существует
     * @throws InvalidEmailException     если email недействителен
     */
    public User handleAuthUserAction(AuthAction action) throws UserNotFoundException, UserAlreadyExistException, InvalidEmailException {
        CreateUserDto dto = ConsoleInHelper.getCreateUserDtoFromInput();
        return switch (action) {
            case LOGIN -> authController.login(dto);
            case REGISTER -> authController.register(dto);
        };
    }
}
