package org.example.core.dtos.user_dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO for user authentication")
public class AuthUserDto {
    @NotNull(message = "Must not be null")
    @NotBlank(message = "Must not be blank")
    @Schema(name = "User's email", example = "user@example.com")
    private String email;


    @NotNull(message = "Must not be null")
    @NotBlank(message = "Must not be blank")
    @Schema(name = "User's password", example = "password")
    private String password;
}
