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

    @Schema(name = "authentication token for the user", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleEBtYWlsLnJ1IiwiZXhwIjoxNzMxMTQxNDk0LCJ1c2VyX2lkIjoxLCJpc19hZG1pbiI6ZmFsc2V9.vgooxCQAqkLj93Ci1NMs47Ol8FMwL7A787IHEch20_A")
    private String token;
}
