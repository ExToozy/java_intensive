package org.example.core.dtos.user_dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "DTO for user authentication")
public class AuthUserDto {
    @NotNull(message = "Must not be null")
    @NotBlank(message = "Must not be blank")
    @ApiModelProperty(value = "User's email", example = "user@example.com", required = true)
    private String email;


    @NotNull(message = "Must not be null")
    @NotBlank(message = "Must not be blank")
    @ApiModelProperty(value = "User's password", example = "password", required = true)
    private String password;
}
