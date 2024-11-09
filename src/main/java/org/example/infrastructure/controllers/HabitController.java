package org.example.infrastructure.controllers;

import com.example.audit_aspect_starter.annotations.Auditable;
import com.example.logging_aspect_starter.annotations.Loggable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.core.dtos.habit_dtos.CreateHabitDto;
import org.example.core.dtos.habit_dtos.UpdateHabitDto;
import org.example.core.models.Habit;
import org.example.core.services.HabitService;
import org.example.exceptions.HabitNotFoundException;
import org.example.exceptions.InvalidTokenException;
import org.example.infrastructure.util.JwtProvider;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Loggable
@RestController
@RequestMapping("api/v1/habits")
@RequiredArgsConstructor
@Tag(name = "Habits", description = "Operations for managing user habits")
public class HabitController {
    private final HabitService habitService;
    private final JwtProvider jwtProvider;

    @Operation(summary = "Retrieve user's habits", description = "Fetches all habits for the authenticated user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved habits"),
            @ApiResponse(responseCode = "401", description = "Invalid token or user unauthorized")
    })
    @Auditable
    @GetMapping
    public List<Habit> getUserHabits(@RequestHeader("Authorization") String token) throws InvalidTokenException {
        int userId = jwtProvider.getUserIdFromToken(token);
        return habitService.getUserHabits(userId);
    }

    @Operation(summary = "Create a new habit", description = "Creates a new habit for the authenticated user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Habit created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "401", description = "Invalid token or user unauthorized")
    })
    @Auditable
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUserHabit(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid CreateHabitDto createHabitDto
    ) throws InvalidTokenException {
        int userIdFromToken = jwtProvider.getUserIdFromToken(token);
        if (userIdFromToken == createHabitDto.getUserId()) {
            habitService.createHabit(createHabitDto);
        }
    }

    @Operation(summary = "Retrieve a habit", description = "Fetches habit by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved habit"),
            @ApiResponse(responseCode = "400", description = "Habit not found"),
            @ApiResponse(responseCode = "401", description = "Invalid token or user unauthorized")
    })
    @Auditable
    @GetMapping("/{habitId}")
    public Habit getUserHabit(
            @RequestHeader("Authorization") String token,
            @PathVariable("habitId") int habitId
    ) throws HabitNotFoundException, InvalidTokenException {
        int userId = jwtProvider.getUserIdFromToken(token);
        return habitService.getUserHabit(userId, habitId);
    }

    @Operation(summary = "Update a habit", description = "Updates details of a habit by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habit updated successfully"),
            @ApiResponse(responseCode = "400", description = "Habit not found or validation error"),
            @ApiResponse(responseCode = "401", description = "Invalid token or user unauthorized")
    })
    @Auditable
    @PutMapping("/{habitId}")
    public void updateUserHabit(
            @RequestHeader("Authorization") String token,
            @PathVariable("habitId") int habitId,
            @RequestBody @Valid UpdateHabitDto updateHabitDto
    ) throws HabitNotFoundException, InvalidTokenException {
        int userId = jwtProvider.getUserIdFromToken(token);
        habitService.updateUserHabit(userId, habitId, updateHabitDto);
    }

    @Operation(summary = "Delete a habit", description = "Deletes a habit by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Habit deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Habit not found"),
            @ApiResponse(responseCode = "401", description = "Invalid token or user unauthorized")
    })
    @Auditable
    @DeleteMapping("/{habitId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUserHabit(
            @RequestHeader("Authorization") String token,
            @PathVariable("habitId") int habitId
    ) throws HabitNotFoundException, InvalidTokenException {
        int userId = jwtProvider.getUserIdFromToken(token);
        habitService.removeUserHabit(userId, habitId);
    }

    @Operation(summary = "Get habit deadline", description = "Fetches the deadline date for a habit")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved deadline"),
            @ApiResponse(responseCode = "400", description = "Habit not found"),
            @ApiResponse(responseCode = "401", description = "Invalid token or user unauthorized")
    })
    @Auditable
    @GetMapping("/{habitId}/deadline")
    public Map<String, LocalDate> getHabitDeadlineDay(
            @RequestHeader("Authorization") String token,
            @PathVariable("habitId") int habitId
    ) throws HabitNotFoundException, InvalidTokenException {
        int userId = jwtProvider.getUserIdFromToken(token);
        return Map.of("deadline", habitService.getHabitDeadlineDay(userId, habitId));
    }

    @Operation(summary = "Check habit completion status", description = "Retrieves the completion status of a habit")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved completion status"),
            @ApiResponse(responseCode = "400", description = "Habit not found"),
            @ApiResponse(responseCode = "401", description = "Invalid token or user unauthorized")
    })
    @Auditable
    @GetMapping("/{habitId}/completion-status")
    public Map<String, Boolean> getHabitCompletionStatus(
            @RequestHeader("Authorization") String token,
            @PathVariable("habitId") int habitId
    ) throws HabitNotFoundException, InvalidTokenException {
        int userId = jwtProvider.getUserIdFromToken(token);
        return Map.of("completion_status", habitService.isCompleteUserHabit(userId, habitId));
    }

    @Operation(summary = "Get statistics for all habits", description = "Retrieves statistics for all habits of the user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved statistics"),
            @ApiResponse(responseCode = "401", description = "Invalid token or user unauthorized")
    })
    @Auditable
    @GetMapping("/statistics")
    public List<Map<String, Object>> getStatisticsOfAllUserHabits(
            @RequestHeader("Authorization") String token
    ) throws InvalidTokenException {
        int userId = jwtProvider.getUserIdFromToken(token);
        return habitService.getStatisticsOfAllUserHabits(userId);
    }

    @Operation(summary = "Get statistics for a specific habit", description = "Retrieves statistics for a specific habit by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved habit statistics"),
            @ApiResponse(responseCode = "400", description = "Habit not found"),
            @ApiResponse(responseCode = "401", description = "Invalid token or user unauthorized")
    })
    @Auditable
    @GetMapping("{habitId}/statistics")
    public Map<String, Object> getStatisticsOfOneUserHabit(
            @RequestHeader("Authorization") String token,
            @PathVariable("habitId") int habitId
    ) throws HabitNotFoundException, InvalidTokenException {
        int userId = jwtProvider.getUserIdFromToken(token);
        return habitService.getStatisticsOfOneUserHabit(userId, habitId);
    }
}
