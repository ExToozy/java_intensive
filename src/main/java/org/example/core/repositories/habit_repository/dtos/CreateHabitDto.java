package org.example.core.repositories.habit_repository.dtos;

import org.example.core.models.HabitFrequency;

public class CreateHabitDto {
    private int userId;
    private String name;
    private String description;
    private HabitFrequency frequency;

    public CreateHabitDto(int userId, String name, String description, HabitFrequency frequency) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.frequency = frequency;
    }


    public int getUserId() {
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
