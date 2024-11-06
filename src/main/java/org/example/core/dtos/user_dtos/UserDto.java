package org.example.core.dtos.user_dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "DTO representing a user")
public final class UserDto {

    @ApiModelProperty(value = "Unique identifier of the user", example = "1")
    private int userId;

    @ApiModelProperty(value = "User's email address", example = "user@example.com")
    private String email;
}
