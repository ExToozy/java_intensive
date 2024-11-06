package org.example.core.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

/**
 * Класс, представляющий запись отметку о выполнении привычки.
 * Содержит: идентификатор, идентификатор привычки и дату выполнения привычки.
 */
@Getter
@AllArgsConstructor
public class HabitTrack {
    private final int id;
    private final int habitId;
    private final LocalDate completeDate;
}
