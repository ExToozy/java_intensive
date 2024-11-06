package org.example.core.dtos.habit_dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.annotations.InEnumValues;
import org.example.core.models.HabitFrequency;


@Data
@Schema(description = "DTO for update habit")
@AllArgsConstructor
@NoArgsConstructor
public final class UpdateHabitDto {

    @NotNull(message = "Must not be null")
    @NotBlank(message = "Must not be blank")
    @Size(min = 3, max = 255)
    @Schema(name = "habit name", example = "Walk 500 steps")
    private String name;

    @NotNull(message = "Must not be null")
    @NotBlank(message = "Can not be blank")
    @Size(min = 3, max = 255)
    @Schema(name = "habit description", example = "I need do it everyday")
    private String description;

    @NotNull(message = "Must not be null")
    @NotBlank(message = "Must not be blank")
    @InEnumValues(enumClass = HabitFrequency.class)
    @Size(min = 3, max = 10, message = "Must be between 3 and 10 characters")
    @Schema(name = "habit frequency", example = "DAILY")
    private String frequency;
}
