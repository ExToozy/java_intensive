package org.example.core.dtos.user_dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO for user authentication")
public class AuthUserDto {
    @NotNull(message = "Email must not be null")
    @NotBlank(message = "Email must not be blank")
    @Size(min = 3, max = 255, message = "Email must be between 3 and 255 characters")
    @Schema(name = "User's email", example = "user@example.com")
    private String email;


    @NotNull(message = "Password must not be null")
    @NotBlank(message = "Password must not be blank")
    @Size(min = 3, max = 255, message = "Password must be between 3 and 255 characters")
    @Schema(name = "User's password", example = "password")
    private String password;
}
