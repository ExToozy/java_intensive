package org.example.core.models;

import java.time.LocalDate;
import java.util.UUID;

public class HabitTrack {
    private UUID id;
    private UUID habitId;
    private LocalDate completeDate;
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
