package org.example.infastructure.data.models;

import java.time.LocalDate;
import java.util.UUID;

public class HabitTrackEntity {
    private UUID id;
    private UUID habitId;
    private LocalDate completeDate;

    public HabitTrackEntity(UUID id, UUID habitId, LocalDate completeDate) {
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
