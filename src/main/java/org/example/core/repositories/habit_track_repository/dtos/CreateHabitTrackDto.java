package org.example.core.repositories.habit_track_repository.dtos;


public class CreateHabitTrackDto {
    private final int habitId;

    public CreateHabitTrackDto(int habitId) {
        this.habitId = habitId;
    }

    public int getHabitId() {
        return habitId;
    }
}
