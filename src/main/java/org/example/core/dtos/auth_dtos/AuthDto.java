package org.example.core.dtos.auth_dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO for returning authentication token information")
public class AuthDto {

    @Schema(name = "authentication token for the user", example = "1")
    private String token;
}
