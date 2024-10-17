package org.example.core.models;

import java.time.LocalDate;
import java.util.UUID;

/**
 *  ласс, представл€ющий запись отметку о выполнении привычки.
 * —одержит: идентификатор, идентификатор привычки и дату выполнени€ привычки.
 */
public class HabitTrack {
    private UUID id;
    private UUID habitId;
    private LocalDate completeDate;

    /**
     * —оздаЄт новую отметку о выполнении привычки.
     *
     * @param id           идентификатор отметки
     * @param habitId      идентификатор привычки
     * @param completeDate дата выполнени€ привычки
     */
    public HabitTrack(UUID id, UUID habitId, LocalDate completeDate) {
        this.id = id;
        this.habitId = habitId;
        this.completeDate = completeDate;
    }

    public UUID getId() {
        return id;
    }

    public UUID getHabitId() {
        return habitId;
    }

    public LocalDate getCompleteDate() {
        return completeDate;
    }
}
