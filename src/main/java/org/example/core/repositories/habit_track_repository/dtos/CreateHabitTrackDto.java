package org.example.core.repositories.habit_track_repository.dtos;

import java.util.UUID;

public class CreateHabitTrackDto {
    private final UUID habitId;

    public CreateHabitTrackDto(UUID habitId) {
        this.habitId = habitId;
    }

    public UUID getHabitId() {
        return habitId;
    }
}
