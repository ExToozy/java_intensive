package org.example.core.repositories.habit_repository.dtos;

import org.example.core.models.HabitFrequency;

import java.util.UUID;

public class CreateHabitDto {
    private UUID userId;
    private String name;
    private String description;
    private HabitFrequency frequency;

    public CreateHabitDto(UUID userId, String name, String description, HabitFrequency frequency) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.frequency = frequency;
    }


    public UUID getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public HabitFrequency getFrequency() {
        return frequency;
    }
}
