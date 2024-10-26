package org.example.core.dtos.habit_track_dtos;


public class CreateHabitTrackDto {
    private final int habitId;

    public CreateHabitTrackDto(int habitId) {
        this.habitId = habitId;
    }

    public int getHabitId() {
        return habitId;
    }
}
