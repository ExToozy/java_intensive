package org.example.infrastructure.data.models;

import org.example.core.models.HabitFrequency;

import java.time.LocalDate;

public class HabitEntity {
    private final int id;
    private final int userId;
    private final LocalDate dayOfCreation;
    private String name;
    private String description;
    private HabitFrequency frequency;

    public HabitEntity(int id, int userId, String name, String description, HabitFrequency frequency, LocalDate dayOfCreation) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.dayOfCreation = dayOfCreation;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HabitFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(HabitFrequency frequency) {
        this.frequency = frequency;
    }

    public LocalDate getDayOfCreation() {
        return dayOfCreation;
    }
}
