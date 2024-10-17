package org.example.core.services;

import org.example.core.exceptions.InvalidEmailException;
import org.example.core.exceptions.UserAlreadyExistException;
import org.example.core.exceptions.UserNotFoundException;
import org.example.core.models.User;
import org.example.core.repositories.user_repository.dtos.CreateUserDto;
import org.example.core.util.PasswordManager;
import org.example.core.util.RegexUtil;

/**
 * ����� ��� ���������� ��������������� �������������.
 * �������� ������ ��� ����� � ����������� ����� �������������.
 */
public class AuthService {

    // ������ ��� ������ � ��������������.
    private final UserService userService;

    /**
     * ����������� ��� {@link AuthService}.
     *
     * @param userService ������ {@link UserService}, ������ ��� ������ � ��������������
     */
    public AuthService(UserService userService) {
        this.userService = userService;
    }

    /**
     * ��������� ���� ������������ �� email � ������.
     *
     * @param dto {@link CreateUserDto} ������ ��� �����, ���������� email � ������
     * @return ������ {@link User}, ���� �������������� �������
     * @throws UserNotFoundException ���� ������������ �� ������ ��� ������ �������
     */
    public User login(CreateUserDto dto) throws UserNotFoundException {
        User user = userService.getUserByEmail(dto.getEmail());
        if (!PasswordManager.checkPasswordEquals(dto.getPassword(), user.getPassword())) {
            throw new UserNotFoundException();
        }
        return user;
    }

    /**
     * ������������ ������ ������������.
     *
     * @param dto ������ ��� �����������, ���������� email � ������
     * @return {@link User}, ��������� ������������
     * @throws SecurityException         ���� ��������� ������ ��� ��������� ���� ������
     * @throws UserAlreadyExistException ���� ������������ � ����� email ��� ����������
     * @throws InvalidEmailException     ���� ������ email �����������
     */
    public User register(CreateUserDto dto) throws SecurityException, UserAlreadyExistException, InvalidEmailException {
        if (RegexUtil.isInvalidEmail(dto.getEmail())) {
            throw new InvalidEmailException();
        }

        if (userService.checkEmailExist(dto.getEmail())) {
            String msg = String.format("User with %s already exist", dto.getEmail());
            throw new UserAlreadyExistException(msg);
        }

        String passwordHash = PasswordManager.getPasswordHash(dto.getPassword());
        if (passwordHash == null) {
            throw new SecurityException("An error occurred while trying to get a password hash");
        }

        dto.setPassword(passwordHash);
        return userService.create(dto);
    }
}
