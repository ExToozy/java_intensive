package org.example.core.dtos.auth_dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "DTO for returning authentication token information")
public class AuthDto {

    @ApiModelProperty(value = "authentication token for the user", example = "1")
    private String token;
}
