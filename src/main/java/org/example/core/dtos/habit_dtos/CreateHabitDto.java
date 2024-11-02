package org.example.core.dtos.habit_dtos;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.annotations.InEnumValues;
import org.example.core.models.HabitFrequency;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@ApiModel(description = "DTO for create habit")
@AllArgsConstructor
@NoArgsConstructor
public final class CreateHabitDto {

    @NotNull(message = "Must not be null")
    @Positive(message = "Must be greater than zero")
    @ApiModelProperty(value = "user id to create", example = "1", required = true)
    private int userId;

    @NotNull(message = "Must not be null")
    @NotBlank(message = "Must not be blank")
    @Size(min = 3, max = 100, message = "Too long")
    @ApiModelProperty(value = "habit name", example = "Walk 500 steps", required = true)
    private String name;

    @NotNull(message = "Must not be null")
    @NotBlank(message = "Can not be blank")
    @ApiModelProperty(value = "habit description", example = "I need do it everyday", required = true)
    @Size(min = 3, max = 255, message = "Too long")
    private String description;

    @NotNull(message = "Must not be null")
    @NotBlank(message = "Must not be blank")
    @ApiModelProperty(value = "habit frequency", example = "DAILY", required = true)
    @InEnumValues(enumClass = HabitFrequency.class)
    private String frequency;
}
