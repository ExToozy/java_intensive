package org.example.infrastructure.data.models;

import java.time.LocalDate;

public class HabitTrackEntity {
    private int id;
    private int habitId;
    private LocalDate completeDate;

    public HabitTrackEntity(int id, int habitId, LocalDate completeDate) {
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
