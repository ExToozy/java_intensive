package org.example.core.dtos.user_dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "DTO for changing the user's admin status")
public class ChangeAdminStatusDto {

    @NotNull
    @ApiModelProperty(value = "Indicates whether the user is an administrator", required = true, example = "true")
    private boolean isAdmin;
}
