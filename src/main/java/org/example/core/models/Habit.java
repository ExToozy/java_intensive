package org.example.core.models;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Класс {@code Habit} представляет привычку, созданную пользователем.
 * Содержит:
 * иденфитикатор привычки,
 * иденфитикатор пользователя,
 * название привычки,
 * описание привычки,
 * частота выполнения,
 * дату создания,
 */
public class Habit {
    private final int id;
    private final int userId;
    private final String name;
    private final String description;
    private final HabitFrequency frequency;
    private final LocalDate dayOfCreation;

    /**
     * Конструктор для создания объекта {@code Habit}.
     *
     * @param id            иденфитикатор привычки
     * @param userId        иденфитикатор пользователя
     * @param name          название привычки
     * @param description   описание привычки
     * @param frequency     частота выполнения
     * @param dayOfCreation дата создания
     */
    public Habit(int id, int userId, String name, String description, HabitFrequency frequency, LocalDate dayOfCreation) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.dayOfCreation = dayOfCreation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, name, description, frequency, dayOfCreation);
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

    public int getUserId() {
        return userId;
    }

    public LocalDate getDayOfCreation() {
        return dayOfCreation;
    }

    public int getId() {
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
