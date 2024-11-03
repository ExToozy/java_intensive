package org.example.infrastructure.controllers;

import lombok.RequiredArgsConstructor;
import org.example.annotations.Auditable;
import org.example.annotations.Loggable;
import org.example.core.dtos.habit_dtos.CreateHabitDto;
import org.example.core.dtos.habit_dtos.UpdateHabitDto;
import org.example.core.models.Habit;
import org.example.core.services.HabitService;
import org.example.exceptions.HabitNotFoundException;
import org.example.exceptions.InvalidTokenException;
import org.example.infrastructure.util.TokenHelper;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Loggable
@RestController
@RequestMapping("api/v2/habits")
@RequiredArgsConstructor
public class HabitController {
    private final HabitService habitService;


    @Auditable
    @GetMapping
    public List<Habit> getUserHabits(@RequestHeader("Authorization") String token) throws InvalidTokenException {
        int userId = TokenHelper.getUserIdFromToken(token);
        return habitService.getUserHabits(userId);
    }

    @Auditable
    @PostMapping
    public void createUserHabit(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid CreateHabitDto createHabitDto
    ) throws InvalidTokenException {
        int userIdFromToken = TokenHelper.getUserIdFromToken(token);
        if (userIdFromToken == createHabitDto.getUserId()) {
            habitService.createHabit(createHabitDto);
        }
    }

    @Auditable
    @GetMapping("/{habitId}")
    public Habit getUserHabit(
            @RequestHeader("Authorization") String token,
            @PathVariable("habitId") int habitId
    ) throws HabitNotFoundException, InvalidTokenException {
        int userId = TokenHelper.getUserIdFromToken(token);
        return habitService.getUserHabit(userId, habitId);
    }

    @Auditable
    @PutMapping("/{habitId}")
    public void updateUserHabit(
            @RequestHeader("Authorization") String token,
            @PathVariable("habitId") int habitId,
            @RequestBody @Valid UpdateHabitDto updateHabitDto
    ) throws HabitNotFoundException, InvalidTokenException {
        int userId = TokenHelper.getUserIdFromToken(token);
        habitService.updateUserHabit(userId, habitId, updateHabitDto);
    }

    @Auditable
    @DeleteMapping("/{habitId}")
    public void removeUserHabit(
            @RequestHeader("Authorization") String token,
            @PathVariable("habitId") int habitId
    ) throws HabitNotFoundException, InvalidTokenException {
        int userId = TokenHelper.getUserIdFromToken(token);
        habitService.removeUserHabit(userId, habitId);
    }

    @Auditable
    @GetMapping("/{habitId}/deadline")
    public Map<String, LocalDate> getHabitDeadlineDay(
            @RequestHeader("Authorization") String token,
            @PathVariable("habitId") int habitId
    ) throws HabitNotFoundException, InvalidTokenException {
        int userId = TokenHelper.getUserIdFromToken(token);
        return Map.of("deadline", habitService.getHabitDeadlineDay(userId, habitId));
    }

    @Auditable
    @GetMapping("/{habitId}/completion-status")
    public Map<String, Boolean> getHabitCompletionStatus(
            @RequestHeader("Authorization") String token,
            @PathVariable("habitId") int habitId
    ) throws HabitNotFoundException, InvalidTokenException {
        int userId = TokenHelper.getUserIdFromToken(token);
        return Map.of("completion_status", habitService.isCompleteUserHabit(userId, habitId));
    }

    @Auditable
    @GetMapping("/statistics")
    public List<Map<String, Object>> getStatisticsOfAllUserHabits(
            @RequestHeader("Authorization") String token
    ) throws InvalidTokenException {
        int userId = TokenHelper.getUserIdFromToken(token);
        return habitService.getStatisticsOfAllUserHabits(userId);
    }

    @Auditable
    @GetMapping("{habitId}/statistics")
    public Map<String, Object> getStatisticsOfOneUserHabit(
            @RequestHeader("Authorization") String token,
            @PathVariable("habitId") int habitId
    ) throws HabitNotFoundException, InvalidTokenException {
        int userId = TokenHelper.getUserIdFromToken(token);
        return habitService.getStatisticsOfOneUserHabit(userId, habitId);
    }
}
