package org.example.core.dtos.user_dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "DTO for updating user details")
public class UpdateUserDto {

    @NotBlank(message = "Must not be blank")
    @ApiModelProperty(value = "User's email address", example = "user@mail.ru")
    private String email;

    @NotBlank(message = "Must not be blank")
    @Size(min = 3, max = 255, message = "Must be between 3 and 255 characters")
    @ApiModelProperty(value = "User's password", example = "password")
    private String password;
}
