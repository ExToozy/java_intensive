package org.example.core.models;

import java.time.Period;

public enum HabitFrequency {
    DAILY("Daily", Period.ofDays(1)),
    WEEKLY("Weekly", Period.ofWeeks(1));

    private final String str;
    private final Period period;

    HabitFrequency(String str, Period period) {
        this.str = str;
        this.period = period;
    }

    @Override
    public String toString() {
        return str;
    }

    public Period toPeriod() {
        return period;
    }
}
