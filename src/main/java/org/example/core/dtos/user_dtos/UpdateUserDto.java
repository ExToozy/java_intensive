package org.example.core.dtos.user_dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for updating user details")
public class UpdateUserDto {

    @NotBlank(message = "Email must not be blank")
    @Size(min = 3, max = 255, message = "Email must be between 3 and 255 characters")
    @Schema(description = "User's email address", example = "user@mail.ru")
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 3, max = 255, message = "Password must be between 3 and 255 characters")
    @Schema(description = "User's password", example = "password")
    private String password;
}
