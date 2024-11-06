package org.example.core.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Period;

/**
 * Перечисление, представляющее частоту выполнения привычки.
 * Содержит два значения: DAILY и WEEKLY
 */
@AllArgsConstructor
@Getter
public enum HabitFrequency {
    DAILY(Period.ofDays(1)),
    WEEKLY(Period.ofWeeks(1));
    private final Period period;
}
