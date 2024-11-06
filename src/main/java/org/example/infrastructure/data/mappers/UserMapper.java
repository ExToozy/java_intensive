package org.example.infrastructure.data.mappers;

import org.example.core.dtos.user_dtos.UserDto;
import org.example.core.models.User;
import org.example.infrastructure.util.MappingUtils;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(uses = MappingUtils.class, componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    @Mapping(source = "domain.id", target = "userId")
    UserDto toUserDto(User domain);

    List<UserDto> toUserDtoList(List<User> users);
}
