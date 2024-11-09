package org.example.infrastructure.controllers;

import com.example.audit_aspect_starter.annotations.Auditable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.core.models.HabitTrack;
import org.example.core.services.HabitService;
import org.example.core.services.HabitTrackService;
import org.example.exceptions.HabitNotFoundException;
import org.example.exceptions.HabitTrackNotFoundException;
import org.example.exceptions.InvalidTokenException;
import org.example.infrastructure.util.JwtProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/tracks", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Habit Tracks", description = "Operations for managing habit tracking")
public class HabitTrackController {

    private final HabitTrackService habitTrackService;
    private final HabitService habitService;
    private final JwtProvider jwtProvider;

    @Operation(summary = "Retrieve tracks for a habit", description = "Fetches all tracking records for a habit by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved habit tracks"),
            @ApiResponse(responseCode = "404", description = "Habit not found"),
            @ApiResponse(responseCode = "401", description = "Invalid token or user unauthorized")
    })
    @Auditable
    @GetMapping("/by-habit-id/{habitId}")
    public List<HabitTrack> getHabitTracks(
            @RequestHeader("Authorization") String token,
            @PathVariable("habitId") int habitId
    ) throws InvalidTokenException, HabitNotFoundException {
        int userId = jwtProvider.getUserIdFromToken(token);
        if (habitService.isUserHabitOrUserIsAdmin(userId, habitId)) {
            return habitTrackService.getHabitTracks(habitId);
        }
        throw new HabitNotFoundException();
    }

    @Operation(summary = "Remove all tracks for a habit", description = "Deletes all tracking records for a habit by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successfully removed habit tracks"),
            @ApiResponse(responseCode = "404", description = "Habit not found"),
            @ApiResponse(responseCode = "401", description = "Invalid token or user unauthorized")
    })
    @Auditable
    @DeleteMapping("/by-habit-id/{habitId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeHabitTracks(
            @RequestHeader("Authorization") String token,
            @PathVariable("habitId") int habitId
    ) throws InvalidTokenException, HabitNotFoundException {
        int userId = jwtProvider.getUserIdFromToken(token);
        if (habitService.isUserHabitOrUserIsAdmin(userId, habitId)) {
            habitTrackService.removeHabitTracks(habitId);
        } else {
            throw new HabitNotFoundException();
        }
    }

    @Operation(summary = "Remove a specific track", description = "Deletes a tracking record by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successfully removed track"),
            @ApiResponse(responseCode = "404", description = "Track not found"),
            @ApiResponse(responseCode = "401", description = "Invalid token or user unauthorized")
    })
    @Auditable
    @DeleteMapping("/{trackId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeTrack(
            @RequestHeader("Authorization") String token,
            @PathVariable("trackId") int trackId
    ) throws InvalidTokenException, HabitTrackNotFoundException {
        int userId = jwtProvider.getUserIdFromToken(token);
        if (habitService.isUserHabitTrackOrUserIsAdmin(userId, trackId)) {
            habitTrackService.remove(trackId);
        } else {
            throw new HabitTrackNotFoundException();
        }
    }

    @Operation(summary = "Mark habit as complete", description = "Marks a habit as completed")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habit marked as complete"),
            @ApiResponse(responseCode = "404", description = "Habit not found"),
            @ApiResponse(responseCode = "401", description = "Invalid token or user unauthorized")
    })
    @Auditable
    @PostMapping("/complete-habit/{habitId}")
    public void completeHabit(
            @RequestHeader("Authorization") String token,
            @PathVariable("habitId") int habitId
    ) throws InvalidTokenException, HabitNotFoundException {
        int userId = jwtProvider.getUserIdFromToken(token);
        if (habitService.isUserHabitOrUserIsAdmin(userId, habitId)) {
            habitTrackService.completeHabit(habitId);
        } else {
            throw new HabitNotFoundException();
        }
    }
}
