package org.example.core.services;

import org.example.core.exceptions.InvalidEmailException;
import org.example.core.exceptions.UserAlreadyExistException;
import org.example.core.exceptions.UserNotFoundException;
import org.example.core.models.User;
import org.example.core.repositories.user_repository.dtos.CreateUserDto;
import org.example.core.util.PasswordManager;
import org.example.core.util.RegexUtil;

/**
 * Класс для управления аутентификацией пользователей.
 * Содержит методы для входа и регистрации новых пользователей.
 */
public class AuthService {

    // Сервис для работы с пользователями.
    private final UserService userService;

    /**
     * Конструктор для {@link AuthService}.
     *
     * @param userService объект {@link UserService}, сервис для работы с пользователями
     */
    public AuthService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Выполняет вход пользователя по email и паролю.
     *
     * @param dto {@link CreateUserDto} данные для входа, включающие email и пароль
     * @return объект {@link User}, если аутентификация успешна
     * @throws UserNotFoundException если пользователь не найден или пароль неверен
     */
    public User login(CreateUserDto dto) throws UserNotFoundException {
        User user = userService.getUserByEmail(dto.getEmail());
        if (!PasswordManager.checkPasswordEquals(dto.getPassword(), user.getPassword())) {
            throw new UserNotFoundException();
        }
        return user;
    }

    /**
     * Регистрирует нового пользователя.
     *
     * @param dto данные для регистрации, включающие email и пароль
     * @return {@link User}, созданный пользователь
     * @throws SecurityException         если произошла ошибка при генерации хэша пароля
     * @throws UserAlreadyExistException если пользователь с таким email уже существует
     * @throws InvalidEmailException     если формат email некорректен
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
