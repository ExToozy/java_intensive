package org.example.infastructure.data.repositories.in_memory_repositories;

import org.example.core.exceptions.UserNotFoundException;
import org.example.core.models.User;
import org.example.core.repositories.user_repository.IUserRepository;
import org.example.core.repositories.user_repository.dtos.ChangeAdminStatusDto;
import org.example.core.repositories.user_repository.dtos.CreateUserDto;
import org.example.core.repositories.user_repository.dtos.UpdateUserDto;
import org.example.core.util.PasswordManager;
import org.example.infastructure.data.mappers.Mapper;
import org.example.infastructure.data.mappers.UserMapper;
import org.example.infastructure.data.models.UserEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class InMemoryUserRepository implements IUserRepository {
    private final Mapper<User, UserEntity> mapper = new UserMapper();
    private final List<UserEntity> users = new ArrayList<>();

    public InMemoryUserRepository() {
        users.addAll(Arrays.asList(
                new UserEntity(
                        UUID.fromString("00000000-0000-0000-0000-000000000000"),
                        "ex@mail.ru",
                        PasswordManager.getPasswordHash("123"),
                        false
                ),

                new UserEntity(
                        UUID.fromString("00000000-0000-0000-0000-000000000001"),
                        "admin",
                        PasswordManager.getPasswordHash("admin"),
                        true
                )
            )
        );
    }

    @Override
    public User create(CreateUserDto dto) {
        UserEntity user = new UserEntity(UUID.randomUUID(), dto.getEmail(), dto.getPassword(), false);
        users.add(user);
        return mapper.toDomain(user);
    }

    @Override
    public User getByEmail(String email) throws UserNotFoundException {
        UserEntity userEntity = users
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElseThrow(UserNotFoundException::new);

        return mapper.toDomain(userEntity);
    }

    @Override
    public List<User> getAll() {
        return users.stream().map(mapper::toDomain).toList();
    }

    @Override
    public void update(UpdateUserDto dto) throws UserNotFoundException {
        UserEntity userEntity = users
                .stream()
                .filter(user -> user.getId().equals(dto.getUserId()))
                .findFirst()
                .orElseThrow(UserNotFoundException::new);

        userEntity.setEmail(dto.getEmail());
        userEntity.setPassword(PasswordManager.getPasswordHash(dto.getPassword()));
    }

    @Override
    public void remove(UUID id) {
        users.removeIf(userEntity -> userEntity.getId().equals(id));
    }

    @Override
    public void changeUserAdminStatus(ChangeAdminStatusDto dto) throws UserNotFoundException {
        UserEntity userEntity = users
                .stream()
                .filter(user -> user.getId().equals(dto.getUserId()))
                .findFirst()
                .orElseThrow(UserNotFoundException::new);

        userEntity.setAdmin(dto.isAdmin());
    }
}
