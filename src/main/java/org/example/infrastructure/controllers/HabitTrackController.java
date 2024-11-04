package org.example.infrastructure.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.example.annotations.Auditable;
import org.example.core.models.HabitTrack;
import org.example.core.services.HabitService;
import org.example.core.services.HabitTrackService;
import org.example.exceptions.HabitNotFoundException;
import org.example.exceptions.HabitTrackNotFoundException;
import org.example.exceptions.InvalidTokenException;
import org.example.infrastructure.util.TokenHelper;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/v1/tracks")
@RequiredArgsConstructor
@Api(tags = "Habit Tracks", description = "Operations for managing habit tracking")
public class HabitTrackController {

    private final HabitTrackService habitTrackService;
    private final HabitService habitService;

    @ApiOperation(value = "Retrieve tracks for a habit", notes = "Fetches all tracking records for a habit by ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved habit tracks"),
            @ApiResponse(code = 400, message = "Habit not found"),
            @ApiResponse(code = 401, message = "Invalid token or user unauthorized")
    })
    @Auditable
    @GetMapping("/by-habit-id/{habitId}")
    public List<HabitTrack> getHabitTracks(
            @RequestHeader("Authorization") String token,
            @PathVariable("habitId") int habitId
    ) throws InvalidTokenException, HabitNotFoundException {
        int userId = TokenHelper.getUserIdFromToken(token);
        if (habitService.isUserHabitOrUserIsAdmin(userId, habitId)) {
            return habitTrackService.getHabitTracks(habitId);
        }
        throw new HabitNotFoundException();
    }

    @ApiOperation(value = "Remove all tracks for a habit", notes = "Deletes all tracking records for a habit by ID")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Successfully removed habit tracks"),
            @ApiResponse(code = 400, message = "Habit not found"),
            @ApiResponse(code = 401, message = "Invalid token or user unauthorized")
    })
    @Auditable
    @DeleteMapping("/by-habit-id/{habitId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeHabitTracks(
            @RequestHeader("Authorization") String token,
            @PathVariable("habitId") int habitId
    ) throws InvalidTokenException, HabitNotFoundException {
        int userId = TokenHelper.getUserIdFromToken(token);
        if (habitService.isUserHabitOrUserIsAdmin(userId, habitId)) {
            habitTrackService.removeHabitTracks(habitId);
        } else {
            throw new HabitNotFoundException();
        }
    }

    @ApiOperation(value = "Remove a specific track", notes = "Deletes a tracking record by ID")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Successfully removed track"),
            @ApiResponse(code = 400, message = "Track not found"),
            @ApiResponse(code = 401, message = "Invalid token or user unauthorized")
    })
    @Auditable
    @DeleteMapping("/{trackId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeTrack(
            @RequestHeader("Authorization") String token,
            @PathVariable("trackId") int trackId
    ) throws InvalidTokenException, HabitTrackNotFoundException {
        int userId = TokenHelper.getUserIdFromToken(token);
        if (habitService.isUserHabitTrackOrUserIsAdmin(userId, trackId)) {
            habitTrackService.remove(trackId);
        } else {
            throw new HabitTrackNotFoundException();
        }
    }

    @ApiOperation(value = "Mark habit as complete", notes = "Marks a habit as completed")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Habit marked as complete"),
            @ApiResponse(code = 400, message = "Habit not found"),
            @ApiResponse(code = 401, message = "Invalid token or user unauthorized")
    })
    @Auditable
    @PostMapping("/complete-habit/{habitId}")
    public void completeHabit(
            @RequestHeader("Authorization") String token,
            @PathVariable("habitId") int habitId
    ) throws InvalidTokenException, HabitNotFoundException {
        int userId = TokenHelper.getUserIdFromToken(token);
        if (habitService.isUserHabitOrUserIsAdmin(userId, habitId)) {
            habitTrackService.completeHabit(habitId);
        } else {
            throw new HabitNotFoundException();
        }
    }
}
