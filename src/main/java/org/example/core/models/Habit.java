package org.example.core.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

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
@Getter
@AllArgsConstructor
public class Habit {
    private final int id;
    private final int userId;
    private final String name;
    private final String description;
    private final HabitFrequency frequency;
    private final LocalDate dayOfCreation;
}
