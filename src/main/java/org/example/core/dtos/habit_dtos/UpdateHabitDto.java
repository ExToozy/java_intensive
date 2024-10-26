package org.example.core.dtos.habit_dtos;

import org.example.core.models.HabitFrequency;

public record UpdateHabitDto(int id, String name, String description, HabitFrequency frequency) {
}
