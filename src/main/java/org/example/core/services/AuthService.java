package org.example.core.services;

import lombok.RequiredArgsConstructor;
import org.example.core.dtos.user_dtos.AuthUserDto;
import org.example.core.models.User;
import org.example.core.util.PasswordManager;
import org.example.core.util.RegexUtil;
import org.example.exceptions.InvalidEmailException;
import org.example.exceptions.UserAlreadyExistException;
import org.example.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Класс для управления аутентификацией пользователей.
 * Содержит методы для входа и регистрации новых пользователей.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;


    /**
     * Выполняет вход пользователя по email и паролю.
     *
     * @param dto {@link AuthUserDto} данные для входа, включающие email и пароль
     * @return объект {@link User}, если аутентификация успешна
     * @throws UserNotFoundException если пользователь не найден или пароль неверен
     */
    public User login(AuthUserDto dto) throws UserNotFoundException {
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
    public User register(AuthUserDto dto) throws SecurityException, UserAlreadyExistException, InvalidEmailException {
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
