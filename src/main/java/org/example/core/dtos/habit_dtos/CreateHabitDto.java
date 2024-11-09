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

    @NotNull(message = "id must not be null")
    @Positive(message = "id must be greater than zero")
    @Schema(name = "user id to create", example = "1")
    private int userId;

    @NotNull(message = "Name must not be null")
    @NotBlank(message = "Name must not be blank")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    @Schema(name = "habit name", example = "Walk 500 steps")
    private String name;

    @NotNull(message = "Description must not be null")
    @NotBlank(message = "Description can not be blank")
    @Size(min = 3, max = 255, message = "Description must be between 3 and 255 characters")
    @Schema(name = "habit description", example = "I need do it everyday")
    private String description;

    @NotNull(message = "Frequency must not be null")
    @NotBlank(message = "Frequency must not be blank")
    @InEnumValues(enumClass = HabitFrequency.class)
    @Size(min = 3, max = 10, message = "Frequency must be between 3 and 10 characters")
    @Schema(name = "habit frequency", example = "DAILY")
    private String frequency;
}
