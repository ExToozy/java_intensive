package org.example.core.services;

import org.example.core.exceptions.InvalidEmail;
import org.example.core.exceptions.UserAlreadyExistException;
import org.example.core.exceptions.UserNotFoundException;
import org.example.core.models.User;
import org.example.core.repositories.user_repository.dtos.CreateUserDto;
import org.example.core.util.PasswordManager;

import java.util.regex.Pattern;

public class AuthService {
    private final UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public User login(CreateUserDto dto) throws UserNotFoundException {
        User user = userService.getUserByEmail(dto.getEmail());
        if (PasswordManager.checkPassword(dto.getPassword())) {
            throw new UserNotFoundException();
        }
        return user;
    }

    public User register(CreateUserDto dto) throws SecurityException, UserAlreadyExistException, InvalidEmail {
        Pattern emailPattern = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        if (!emailPattern.matcher(dto.getEmail()).matches()) {
            throw new InvalidEmail();
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
