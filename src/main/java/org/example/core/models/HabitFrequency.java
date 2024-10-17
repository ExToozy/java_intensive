package org.example.core.models;

import java.time.Period;

/**
 * ѕеречисление, представл€ющее частоту выполнени€ привычки.
 * —одержит два значени€: DAILY и WEEKLY
 */
public enum HabitFrequency {
    DAILY("≈жедневно", Period.ofDays(1)),
    WEEKLY("≈женедельно", Period.ofWeeks(1));

    private final String str;

    private final Period period;

    /**
     *  онструктор дл€ создани€ объекта {@code HabitFrequency}.
     *
     * @param str    строковое представление частоты выполнени€ привычки
     * @param period {@link Period}, €вл€етс€ длительностью между повторени€ми привычки
     */
    HabitFrequency(String str, Period period) {
        this.str = str;
        this.period = period;
    }

    /**
     * ¬озвращает строковое представление частоты.
     *
     * @return строковое описание
     */
    @Override
    public String toString() {
        return str;
    }

    /**
     * ¬озвращает период, св€занный с частотой выполнени€ привычки.
     *
     * @return {@link Period} частоты выполнени€ привычки
     */
    public Period toPeriod() {
        return period;
    }
}
