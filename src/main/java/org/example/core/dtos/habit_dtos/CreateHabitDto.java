package org.example.core.dtos.habit_dtos;

import org.example.core.models.HabitFrequency;

public record CreateHabitDto(int userId, String name, String description, HabitFrequency frequency) {
}
