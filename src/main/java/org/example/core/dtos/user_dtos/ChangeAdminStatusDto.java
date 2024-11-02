package org.example.core.dtos.user_dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeAdminStatusDto {
    @NotNull
    private boolean isAdmin;
}
