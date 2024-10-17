package org.example.infastructure.data.models;

import org.example.core.models.HabitFrequency;

import java.time.LocalDate;
import java.util.UUID;

public class HabitEntity {
    private final UUID id;
    private final UUID userID;
    private final LocalDate dayOfCreation;
    private String name;
    private String description;
    private HabitFrequency frequency;

    public HabitEntity(UUID id, UUID userID, String name, String description, HabitFrequency frequency, LocalDate dayOfCreation) {
        this.id = id;
        this.userID = userID;
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.dayOfCreation = dayOfCreation;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserID() {
        return userID;
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
