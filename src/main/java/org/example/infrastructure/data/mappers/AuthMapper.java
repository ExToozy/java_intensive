package org.example.infrastructure.data.mappers;

import org.example.core.dtos.auth_dtos.AuthDto;
import org.example.core.models.User;
import org.example.infrastructure.util.MappingUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = MappingUtils.class, componentModel = "spring")
public interface AuthMapper {

    @Mapping(source = "user.id", target = "token")
    AuthDto toAuthDtoMap(User user);
}
