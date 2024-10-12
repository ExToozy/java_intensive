package org.example.core.services;

import org.example.core.exceptions.UserNotFoundException;
import org.example.core.models.User;
import org.example.core.repositories.user_repository.IUserRepository;
import org.example.core.repositories.user_repository.dtos.ChangeAdminStatusDto;
import org.example.core.repositories.user_repository.dtos.CreateUserDto;
import org.example.core.repositories.user_repository.dtos.UpdateUserDto;

import java.util.List;
import java.util.UUID;

public class UserService {
    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(CreateUserDto dto) {
        return userRepository.create(dto);
    }

    public void remove(UUID id) {
        userRepository.remove(id);
    }

    public User getUserByEmail(String email) throws UserNotFoundException {
        return userRepository.getByEmail(email);
    }

    public boolean checkEmailExist(String email) {
        try {
            userRepository.getByEmail(email);
            return true;
        } catch (UserNotFoundException e) {
            return false;
        }
    }

    public List<User> getAll() {
        return userRepository.getAll();
    }

    public void changeUserAdminStatus(ChangeAdminStatusDto dto) throws UserNotFoundException {
        userRepository.changeUserAdminStatus(dto);
    }

    public void update(UpdateUserDto dto) {
    }
}
