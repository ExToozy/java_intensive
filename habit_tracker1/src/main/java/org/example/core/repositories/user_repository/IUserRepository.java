package org.example.core.repositories.user_repository;

import org.example.core.exceptions.UserNotFoundException;
import org.example.core.models.User;
import org.example.core.repositories.user_repository.dtos.ChangeAdminStatusDto;
import org.example.core.repositories.user_repository.dtos.CreateUserDto;
import org.example.core.repositories.user_repository.dtos.UpdateUserDto;

import java.util.List;
import java.util.UUID;

public interface IUserRepository {
    User create(CreateUserDto dto);


    User getByEmail(String email) throws UserNotFoundException;

    List<User> getAll();

    void update(UpdateUserDto dto) throws UserNotFoundException;

    void remove(UUID id);

    void changeUserAdminStatus(ChangeAdminStatusDto dto) throws UserNotFoundException;
}
