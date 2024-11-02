package org.example.infrastructure.controllers;

import lombok.RequiredArgsConstructor;
import org.example.annotations.Auditable;
import org.example.core.models.HabitTrack;
import org.example.core.services.HabitService;
import org.example.core.services.HabitTrackService;
import org.example.exceptions.HabitNotFoundException;
import org.example.exceptions.HabitTrackNotFoundException;
import org.example.exceptions.InvalidTokenException;
import org.example.infrastructure.util.TokenHelper;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/v2/tracks")
@RequiredArgsConstructor
public class HabitTrackController {

    private final HabitTrackService habitTrackService;
    private final HabitService habitService;

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

    @Auditable
    @DeleteMapping("/by-habit-id/{habitId}")
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

    @Auditable
    @DeleteMapping("/{trackId}")
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

    @Auditable
    @PostMapping("/complete-habit/{habitId}")
    public void completeHabit(
            @RequestHeader("Authorization") String token,
            @PathVariable("habitId") int habitId
    ) throws InvalidTokenException, HabitNotFoundException {
        habitTrackService.completeHabit(habitId);
        int userId = TokenHelper.getUserIdFromToken(token);
        if (habitService.isUserHabitOrUserIsAdmin(userId, habitId)) {
            habitTrackService.completeHabit(habitId);
        } else {
            throw new HabitNotFoundException();
        }
    }
}
