package org.example.infastructure.controllers.console;

import org.example.core.exceptions.InvalidEmail;
import org.example.core.exceptions.UserAlreadyExistException;
import org.example.core.exceptions.UserNotFoundException;
import org.example.core.models.User;
import org.example.core.repositories.user_repository.dtos.CreateUserDto;
import org.example.core.services.AuthService;

public class ConsoleAuthController {
    private AuthService authService;

    public ConsoleAuthController(AuthService authService) {
        this.authService = authService;
    }

    public User login(CreateUserDto dto) throws UserNotFoundException {
        return authService.login(dto);
    }

    public User register(CreateUserDto dto) throws UserAlreadyExistException, InvalidEmail {
        return authService.register(dto);
    }
}
