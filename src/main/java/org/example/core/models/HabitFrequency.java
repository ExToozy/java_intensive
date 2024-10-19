package org.example.core.models;

import java.time.Period;

/**
 * Перечисление, представляющее частоту выполнения привычки.
 * Содержит два значения: DAILY и WEEKLY
 */
public enum HabitFrequency {
    DAILY("Daily", Period.ofDays(1)),
    WEEKLY("Weekly", Period.ofWeeks(1));

    private final String str;

    private final Period period;

    /**
     * Конструктор для создания объекта {@code HabitFrequency}.
     *
     * @param str    строковое представление частоты выполнения привычки
     * @param period {@link Period}, является длительностью между повторениями привычки
     */
    HabitFrequency(String str, Period period) {
        this.str = str;
        this.period = period;
    }

    /**
     * Возвращает строковое представление частоты.
     *
     * @return строковое описание
     */
    public String toStringRepresentation() {
        return str;
    }

    /**
     * Возвращает период, связанный с частотой выполнения привычки.
     *
     * @return {@link Period} частоты выполнения привычки
     */
    public Period toPeriod() {
        return period;
    }


}
