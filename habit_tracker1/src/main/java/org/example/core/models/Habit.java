package org.example.core.models;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Habit {
    private UUID id;
    private UUID userId;
    private String name;
    private String description;
    private HabitFrequency frequency;
    private LocalDate dayOfCreation;

    public Habit(UUID id, UUID userId, String name, String description, HabitFrequency frequency, LocalDate dayOfCreation) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.dayOfCreation = dayOfCreation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Habit habit = (Habit) o;
        return Objects.equals(id, habit.id)
                && Objects.equals(userId, habit.userId)
                && Objects.equals(name, habit.name)
                && Objects.equals(description, habit.description)
                && frequency == habit.frequency
                && Objects.equals(dayOfCreation, habit.dayOfCreation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, name, description, frequency, dayOfCreation);
    }

    public UUID getUserId() {
        return userId;
    }

    public LocalDate getDayOfCreation() {
        return dayOfCreation;
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
