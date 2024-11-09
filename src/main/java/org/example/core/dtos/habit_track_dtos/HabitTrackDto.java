package org.example.core.dtos.habit_track_dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class HabitTrackDto {
    @Schema(description = "habit id", example = "1")
    private int id;

    @Schema(description = "habit id", example = "1")
    private int habitId;

    @Schema(description = "Habit completion day", example = "[2024, 10, 30]")
    private String completeDate;
}
