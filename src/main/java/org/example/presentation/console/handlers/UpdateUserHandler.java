package org.example.presentation.console.handlers;

import org.example.core.exceptions.InvalidEmailException;
import org.example.core.exceptions.UserNotFoundException;
import org.example.core.models.User;
import org.example.core.repositories.user_repository.dtos.UpdateUserDto;
import org.example.infastructure.controllers.console.ConsoleUserController;
import org.example.presentation.console.actions.UpdateUserAction;
import org.example.presentation.console.in.ConsoleInHelper;
import org.example.presentation.console.out.ConsoleOutHelper;

/**
 * ���������� �������� ���������� ������������.
 * ������������ ���������� ���������� � ������������.
 */
public class UpdateUserHandler {
    private final ConsoleUserController userController;

    /**
     * ����������� UpdateUserHandler.
     *
     * @param userController ���������� ��� ������ � ��������������
     */
    public UpdateUserHandler(ConsoleUserController userController) {
        this.userController = userController;
    }

    /**
     * ������������ �������� ���������� ������������.
     *
     * @param action �������� ���������� ������������
     * @param user   ������������ ��� ����������
     * @return ���� ��������� ����������
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
     * ������������ ���������� email ������������.
     *
     * @param user ������������ ��� ����������
     * @throws UserNotFoundException ���� ������������ �� ������
     * @throws InvalidEmailException ���� ������������ ��� ������������ email
     */
    private void handleUpdateUserEmailAction(User user) throws UserNotFoundException, InvalidEmailException {
        String email = ConsoleOutHelper.getUserEmailFromInput();
        userController.updateUser(new UpdateUserDto(user.getId(), email, user.getEmail()));
    }

    /**
     * ������������ ���������� ������ ������������.
     *
     * @param user ������������ ��� ����������
     * @throws UserNotFoundException ���� ������������ �� ������
     * @throws InvalidEmailException ���� ������������ ��� ������������ email
     */
    private void handleUpdateUserPasswordAction(User user) throws UserNotFoundException, InvalidEmailException {
        String password = ConsoleInHelper.getUserPasswordFromInput();
        userController.updateUser(new UpdateUserDto(user.getId(), user.getEmail(), password));
    }
}
