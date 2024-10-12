package org.example.infastructure.controllers.console;

import org.example.core.exceptions.UserNotFoundException;
import org.example.core.models.User;
import org.example.core.repositories.user_repository.dtos.ChangeAdminStatusDto;
import org.example.core.repositories.user_repository.dtos.UpdateUserDto;
import org.example.core.services.UserService;

import java.util.List;
import java.util.UUID;

public class ConsoleUserController {
    private final UserService userService;

    public ConsoleUserController(UserService userService) {
        this.userService = userService;
    }

    public void updateUser(UpdateUserDto dto) {
        userService.update(dto);
    }

    public void changeUserAdminStatus(ChangeAdminStatusDto dto) throws UserNotFoundException {
        userService.changeUserAdminStatus(dto);

    }

    public List<User> getAll() {
        return userService.getAll();

    }

    public void deleteUser(UUID userId) {
        userService.remove(userId);
    }

}
