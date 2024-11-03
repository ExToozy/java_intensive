package org.example.infrastructure.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Loggable
@RestController
@RequestMapping("api/v2/habits")
@RequiredArgsConstructor
@Api(tags = "Habits", description = "Operations for managing user habits")
public class HabitController {
    private final HabitService habitService;

    @ApiOperation(value = "Retrieve user's habits", notes = "Fetches all habits for the authenticated user")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved habits"),
            @ApiResponse(code = 401, message = "Invalid token or user unauthorized")
    })
    @Auditable
    @GetMapping
    public List<Habit> getUserHabits(@RequestHeader("Authorization") String token) throws InvalidTokenException {
        int userId = TokenHelper.getUserIdFromToken(token);
        return habitService.getUserHabits(userId);
    }

    @ApiOperation(value = "Create a new habit", notes = "Creates a new habit for the authenticated user")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Habit created successfully"),
            @ApiResponse(code = 400, message = "Validation error"),
            @ApiResponse(code = 401, message = "Invalid token or user unauthorized")
    })
    @Auditable
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUserHabit(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid CreateHabitDto createHabitDto
    ) throws InvalidTokenException {
        int userIdFromToken = TokenHelper.getUserIdFromToken(token);
        if (userIdFromToken == createHabitDto.getUserId()) {
            habitService.createHabit(createHabitDto);
        }
    }

    @ApiOperation(value = "Retrieve a habit", notes = "Fetches habit by ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved habit"),
            @ApiResponse(code = 400, message = "Habit not found"),
            @ApiResponse(code = 401, message = "Invalid token or user unauthorized")
    })
    @Auditable
    @GetMapping("/{habitId}")
    public Habit getUserHabit(
            @RequestHeader("Authorization") String token,
            @PathVariable("habitId") int habitId
    ) throws HabitNotFoundException, InvalidTokenException {
        int userId = TokenHelper.getUserIdFromToken(token);
        return habitService.getUserHabit(userId, habitId);
    }

    @ApiOperation(value = "Update a habit", notes = "Updates details of a habit by ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Habit updated successfully"),
            @ApiResponse(code = 400, message = "Habit not found or validation error"),
            @ApiResponse(code = 401, message = "Invalid token or user unauthorized")
    })
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

    @ApiOperation(value = "Delete a habit", notes = "Deletes a habit by ID")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Habit deleted successfully"),
            @ApiResponse(code = 400, message = "Habit not found"),
            @ApiResponse(code = 401, message = "Invalid token or user unauthorized")
    })
    @Auditable
    @DeleteMapping("/{habitId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUserHabit(
            @RequestHeader("Authorization") String token,
            @PathVariable("habitId") int habitId
    ) throws HabitNotFoundException, InvalidTokenException {
        int userId = TokenHelper.getUserIdFromToken(token);
        habitService.removeUserHabit(userId, habitId);
    }

    @ApiOperation(value = "Get habit deadline", notes = "Fetches the deadline date for a habit")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved deadline"),
            @ApiResponse(code = 400, message = "Habit not found"),
            @ApiResponse(code = 401, message = "Invalid token or user unauthorized")
    })
    @Auditable
    @GetMapping("/{habitId}/deadline")
    public Map<String, LocalDate> getHabitDeadlineDay(
            @RequestHeader("Authorization") String token,
            @PathVariable("habitId") int habitId
    ) throws HabitNotFoundException, InvalidTokenException {
        int userId = TokenHelper.getUserIdFromToken(token);
        return Map.of("deadline", habitService.getHabitDeadlineDay(userId, habitId));
    }

    @ApiOperation(value = "Check habit completion status", notes = "Retrieves the completion status of a habit")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved completion status"),
            @ApiResponse(code = 400, message = "Habit not found"),
            @ApiResponse(code = 401, message = "Invalid token or user unauthorized")
    })
    @Auditable
    @GetMapping("/{habitId}/completion-status")
    public Map<String, Boolean> getHabitCompletionStatus(
            @RequestHeader("Authorization") String token,
            @PathVariable("habitId") int habitId
    ) throws HabitNotFoundException, InvalidTokenException {
        int userId = TokenHelper.getUserIdFromToken(token);
        return Map.of("completion_status", habitService.isCompleteUserHabit(userId, habitId));
    }

    @ApiOperation(value = "Get statistics for all habits", notes = "Retrieves statistics for all habits of the user")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved statistics"),
            @ApiResponse(code = 401, message = "Invalid token or user unauthorized")
    })
    @Auditable
    @GetMapping("/statistics")
    public List<Map<String, Object>> getStatisticsOfAllUserHabits(
            @RequestHeader("Authorization") String token
    ) throws InvalidTokenException {
        int userId = TokenHelper.getUserIdFromToken(token);
        return habitService.getStatisticsOfAllUserHabits(userId);
    }

    @ApiOperation(value = "Get statistics for a specific habit", notes = "Retrieves statistics for a specific habit by ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved habit statistics"),
            @ApiResponse(code = 400, message = "Habit not found"),
            @ApiResponse(code = 401, message = "Invalid token or user unauthorized")
    })
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
