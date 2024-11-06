package org.example.core.dtos.habit_track_dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HabitTrackDto {
    @ApiModelProperty(value = "habit id", example = "1", required = true)
    private int id;

    @ApiModelProperty(value = "habit id", example = "1", required = true)
    private int habitId;

    @ApiModelProperty(value = "Habit completion day", example = "[2024, 10, 30]", required = true)
    private String completeDate;
}
