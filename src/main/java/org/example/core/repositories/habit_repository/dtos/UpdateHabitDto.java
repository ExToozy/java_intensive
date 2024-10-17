package org.example.core.repositories.habit_repository.dtos;

import org.example.core.models.HabitFrequency;

import java.util.UUID;

public class UpdateHabitDto {
    private UUID id;
    private String name;
    private String description;
    private HabitFrequency frequency;

    public UpdateHabitDto(UUID id, String name, String description, HabitFrequency frequency) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.frequency = frequency;
    }

    public UUID getId() {
        return id;
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
