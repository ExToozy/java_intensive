package org.example.infrastructure.data.repositories.in_memory_repositories;

import org.example.core.dtos.user_dtos.AuthUserDto;
import org.example.core.dtos.user_dtos.ChangeAdminStatusDto;
import org.example.core.dtos.user_dtos.UpdateUserDto;
import org.example.core.exceptions.UserNotFoundException;
import org.example.core.models.User;
import org.example.core.repositories.IUserRepository;
import org.example.core.util.PasswordManager;
import org.example.infrastructure.data.mappers.UserMapper;
import org.example.infrastructure.data.models.UserEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InMemoryUserRepository implements IUserRepository {
    private final UserMapper mapper = UserMapper.INSTANCE;
    private final List<UserEntity> users = new ArrayList<>();

    public InMemoryUserRepository() {
        users.addAll(Arrays.asList(
                        new UserEntity(
                                1,
                                "ex@mail.ru",
                                PasswordManager.getPasswordHash("123"),
                                false
                        ),

                        new UserEntity(
                                2,
                                "admin",
                                PasswordManager.getPasswordHash("admin"),
                                true
                        )
                )
        );
    }

    @Override
    public User create(AuthUserDto dto) {
        int nextIndex = users.stream().mapToInt(UserEntity::getId).max().orElse(1);
        UserEntity user = new UserEntity(nextIndex, dto.getEmail(), dto.getPassword(), false);
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
                .filter(user -> user.getId() == dto.getUserId())
                .findFirst()
                .orElseThrow(UserNotFoundException::new);

        userEntity.setEmail(dto.getEmail());
        userEntity.setPassword(dto.getPassword());
    }

    @Override
    public void remove(int id) {
        users.removeIf(userEntity -> userEntity.getId() == id);
    }

    @Override
    public void changeUserAdminStatus(ChangeAdminStatusDto dto) throws UserNotFoundException {
        UserEntity userEntity = users
                .stream()
                .filter(user -> user.getId() == dto.getUserId())
                .findFirst()
                .orElseThrow(UserNotFoundException::new);

        userEntity.setAdmin(dto.isAdmin());
    }

    @Override
    public User getById(int id) throws UserNotFoundException {
        UserEntity userEntity = users
                .stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElseThrow(UserNotFoundException::new);

        return mapper.toDomain(userEntity);
    }
}
