package org.example.infastructure.controllers.console;

import org.example.core.models.Habit;
import org.example.core.models.HabitTrack;
import org.example.core.repositories.habit_track_repository.dtos.CreateHabitTrackDto;
import org.example.core.services.HabitTrackService;

import java.util.List;
import java.util.UUID;

public class ConsoleHabitTrackController {
    private final HabitTrackService habitTrackService;

    public ConsoleHabitTrackController(HabitTrackService habitTrackService) {
        this.habitTrackService = habitTrackService;
    }

    public void completeHabit(CreateHabitTrackDto dto) {
        habitTrackService.completeHabit(dto);
    }

    public List<HabitTrack> getHabitTracks(UUID habitId) {
        return habitTrackService.getHabitTracks(habitId);
    }

    public boolean isCompleteHabit(Habit habit) {
        return habitTrackService.isCompleteHabit(habit);
    }
}
