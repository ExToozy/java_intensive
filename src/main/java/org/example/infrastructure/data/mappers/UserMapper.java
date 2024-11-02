package org.example.infrastructure.data.mappers;

import org.example.core.dtos.user_dtos.AuthUserDto;
import org.example.core.dtos.user_dtos.ChangeAdminStatusDto;
import org.example.core.dtos.user_dtos.UpdateUserDto;
import org.example.core.dtos.user_dtos.UserDto;
import org.example.core.models.User;
import org.example.infrastructure.data.models.UserEntity;
import org.example.infrastructure.util.MapperConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

@Mapper(uses = MapperConverter.class)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "userEntity.admin", target = "isAdmin")
    User toDomain(UserEntity userEntity);

    UserEntity toEntity(User domain);

    @Mapping(source = "domain.id", target = "userId")
    UserDto toUserDto(User domain);

    List<UserDto> toUserDtoList(List<User> users);

    ChangeAdminStatusDto toChangeAdminStatusDto(Map<String, Object> jsonMap);

    AuthUserDto toAuthUserDto(Map<String, Object> jsonMap);

    UpdateUserDto toUpdateUserDto(Map<String, Object> jsonMap);
}
