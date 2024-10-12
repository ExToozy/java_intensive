package org.example.core.repositories.habit_track_repository;

import org.example.core.models.HabitTrack;
import org.example.core.repositories.habit_track_repository.dtos.CreateHabitTrackDto;

import java.util.List;
import java.util.UUID;

public interface IHabitTrackRepository {
    void create(CreateHabitTrackDto dto);

    List<HabitTrack> getHabitTracks(UUID habitId);
    
    void removeByHabitId(UUID id);
}
