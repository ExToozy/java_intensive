package org.example.core.dtos.user_dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO for changing the user's admin status")
public class ChangeAdminStatusDto {

    @NotNull(message = "isAdmin must not be null")
    @Schema(description = "Indicates whether the user is an administrator", example = "true")
    private boolean isAdmin;
}
