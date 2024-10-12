package org.example.infastructure.controllers.console;

import org.example.core.models.Habit;
import org.example.core.repositories.habit_repository.dtos.CreateHabitDto;
import org.example.core.repositories.habit_repository.dtos.UpdateHabitDto;
import org.example.core.services.HabitService;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ConsoleHabitController {
    private final HabitService habitService;

    public ConsoleHabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    public void createHabit(CreateHabitDto dto) {
        habitService.createHabit(dto);
    }

    public List<Habit> getUserHabits(UUID userId) {
        return habitService.getUserHabits(userId);
    }

    public void updateHabit(UpdateHabitDto dto) {
        habitService.updateHabit(dto);
    }

    public void removeHabitAndTracks(UUID habitId) {
        habitService.removeHabitAndTracks(habitId);
    }

    public int getHabitExecutionsCountByPeriod(Habit habit, Period period) {
        return habitService.getHabitExecutionCountByPeriod(habit, period);
    }

    public Map<Habit, Map<String, Integer>> getHabitStatistics(UUID userId) {
        return habitService.getHabitStatistics(userId);
    }

    public List<Habit> getUserHabitsByCompleteStatus(UUID userId, boolean isComplete) {
        return habitService.getUserHabitsByCompleteStatus(userId, isComplete);
    }

    public int getHabitStreak(Habit habit) {
        return habitService.getHabitStreak(habit);
    }

    public LocalDate getHabitDeadlineDay(Habit habit) {
        return habitService.getHabitDeadlineDay(habit);
    }

}
