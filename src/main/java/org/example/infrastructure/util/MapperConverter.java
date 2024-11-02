package org.example.infrastructure.util;

import org.example.core.models.Habit;
import org.example.core.models.HabitFrequency;
import org.mapstruct.Named;

public class MapperConverter {
    @Named("frequencyStrToEnum")
    public HabitFrequency frequencyStrToEnum(Object frequency) {
        return HabitFrequency.valueOf(frequency.toString());
    }

    public String mapToString(Object value) {
        return value != null ? value.toString() : null;
    }

    public int mapToInt(Object value) {
        return (Integer) value;
    }

    public int mapHabitToId(Habit value) {
        return value.getId();
    }

    public boolean mapToBoolean(Object value) {
        return (Boolean) value;
    }
}
