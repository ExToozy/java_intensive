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
import javax.validation.constraints.Size;

@Data
@ApiModel(description = "DTO for update habit")
@AllArgsConstructor
@NoArgsConstructor
public final class UpdateHabitDto {

    @NotNull(message = "Must not be null")
    @NotBlank(message = "Must not be blank")
    @Size(min = 3, max = 255)
    @ApiModelProperty(value = "habit name", example = "Walk 500 steps", required = true)
    private String name;

    @NotNull(message = "Must not be null")
    @NotBlank(message = "Can not be blank")
    @Size(min = 3, max = 255)
    @ApiModelProperty(value = "habit description", example = "I need do it everyday", required = true)
    private String description;

    @NotNull(message = "Must not be null")
    @NotBlank(message = "Must not be blank")
    @InEnumValues(enumClass = HabitFrequency.class)
    @Size(min = 3, max = 10, message = "Must be between 3 and 10 characters")
    @ApiModelProperty(value = "habit frequency", example = "DAILY", required = true)
    private String frequency;
}
