package org.example.infastructure.data.mappers;

import org.example.core.models.User;
import org.example.infastructure.data.models.UserEntity;

public class UserMapper implements Mapper<User, UserEntity> {
    @Override
    public User toDomain(UserEntity userEntity) {
        return new User(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.isAdmin()
        );
    }

    @Override
    public UserEntity toEntity(User domain) {
        return new UserEntity(
                domain.getId(),
                domain.getEmail(),
                domain.getPassword(),
                domain.isAdmin()
        );
    }
}
