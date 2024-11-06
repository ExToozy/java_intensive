package org.example.core.dtos.user_dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO representing a user")
public final class UserDto {

    @Schema(name = "Unique identifier of the user", example = "1")
    private int userId;

    @Schema(name = "User's email address", example = "user@example.com")
    private String email;
}
