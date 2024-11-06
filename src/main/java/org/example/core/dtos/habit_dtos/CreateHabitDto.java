package org.example.core.dtos.habit_dtos;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.annotations.InEnumValues;
import org.example.core.models.HabitFrequency;


@Data
@Schema(description = "DTO for create habit")
@AllArgsConstructor
@NoArgsConstructor
public final class CreateHabitDto {

    @NotNull(message = "Must not be null")
    @Positive(message = "Must be greater than zero")
    @Schema(name = "user id to create", example = "1")
    private int userId;

    @NotNull(message = "Must not be null")
    @NotBlank(message = "Must not be blank")
    @Size(min = 3, max = 100, message = "Too long")
    @Schema(name = "habit name", example = "Walk 500 steps")
    private String name;

    @NotNull(message = "Must not be null")
    @NotBlank(message = "Can not be blank")
    @Schema(name = "habit description", example = "I need do it everyday")
    @Size(min = 3, max = 255, message = "Too long")
    private String description;

    @NotNull(message = "Must not be null")
    @NotBlank(message = "Must not be blank")
    @Schema(name = "habit frequency", example = "DAILY")
    @InEnumValues(enumClass = HabitFrequency.class)
    private String frequency;
}
