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
 * ���������� �������� ��������������.
 * ������������ �������� ����� � ����������� �������������.
 */
public class AuthActionHandler {
    private final ConsoleAuthController authController;

    /**
     * ����������� AuthActionHandler.
     *
     * @param authController ���������� ��� ������ � ���������������
     */
    public AuthActionHandler(ConsoleAuthController authController) {
        this.authController = authController;
    }

    /**
     * ������������ �������� �������������� ������������.
     *
     * @param action �������� ��������������
     * @return ������������������� ������������
     * @throws UserNotFoundException     ���� ������������ �� ������
     * @throws UserAlreadyExistException ���� ������������ ��� ����������
     * @throws InvalidEmailException     ���� email ��������������
     */
    public User handleAuthUserAction(AuthAction action) throws UserNotFoundException, UserAlreadyExistException, InvalidEmailException {
        CreateUserDto dto = ConsoleInHelper.getCreateUserDtoFromInput();
        return switch (action) {
            case LOGIN -> authController.login(dto);
            case REGISTER -> authController.register(dto);
        };
    }
}
