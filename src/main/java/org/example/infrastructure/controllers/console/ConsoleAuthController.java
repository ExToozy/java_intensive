package org.example.infrastructure.controllers.console;

import org.example.core.dtos.user_dtos.AuthUserDto;
import org.example.core.exceptions.InvalidEmailException;
import org.example.core.exceptions.UserAlreadyExistException;
import org.example.core.exceptions.UserNotFoundException;
import org.example.core.models.User;
import org.example.core.services.AuthService;

public class ConsoleAuthController {
    private final AuthService authService;

    public ConsoleAuthController(AuthService authService) {
        this.authService = authService;
    }

    public User login(AuthUserDto dto) throws UserNotFoundException {
        return authService.login(dto);
    }

    public User register(AuthUserDto dto) throws UserAlreadyExistException, InvalidEmailException {
        return authService.register(dto);
    }
}
