package org.example.core.dtos.user_dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {

    @NotBlank(message = "Must not be blank")
    private String email;

    @NotBlank(message = "Must not be blank")
    @Size(min = 3, max = 255, message = "Must be between 3 and 255 characters")
    private String password;
}
