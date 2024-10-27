package org.example.core.models;

import java.time.LocalDate;

/**
 * Класс, представляющий запись отметку о выполнении привычки.
 * Содержит: идентификатор, идентификатор привычки и дату выполнения привычки.
 */
public class HabitTrack {
    private final int id;
    private final int habitId;
    private final LocalDate completeDate;

    /**
     * Создаёт новую отметку о выполнении привычки.
     *
     * @param id           идентификатор отметки
     * @param habitId      идентификатор привычки
     * @param completeDate дата выполнения привычки
     */
    public HabitTrack(int id, int habitId, LocalDate completeDate) {
        this.id = id;
        this.habitId = habitId;
        this.completeDate = completeDate;
    }

    public int getId() {
        return id;
    }

    public int getHabitId() {
        return habitId;
    }

    public LocalDate getCompleteDate() {
        return completeDate;
    }
}
