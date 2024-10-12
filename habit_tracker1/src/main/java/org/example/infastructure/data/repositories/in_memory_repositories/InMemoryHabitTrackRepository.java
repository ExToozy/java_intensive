package org.example.infastructure.data.repositories.in_memory_repositories;

import org.example.core.models.HabitTrack;
import org.example.core.repositories.habit_track_repository.IHabitTrackRepository;
import org.example.core.repositories.habit_track_repository.dtos.CreateHabitTrackDto;
import org.example.infastructure.data.mappers.HabitTrackMapper;
import org.example.infastructure.data.mappers.Mapper;
import org.example.infastructure.data.models.HabitTrackEntity;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InMemoryHabitTrackRepository implements IHabitTrackRepository {
    private final List<HabitTrackEntity> tracks = new ArrayList<>();
    private final Mapper<HabitTrack, HabitTrackEntity> mapper = new HabitTrackMapper();

    public InMemoryHabitTrackRepository() {
        tracks.add(
                new HabitTrackEntity(
                        UUID.fromString("00000000-0000-0000-0000-000000000000"),
                        UUID.fromString("00000000-0000-0000-0000-000000000000"),
                        LocalDate.now()
                )
        );
        tracks.add(
                new HabitTrackEntity(
                        UUID.fromString("00000000-0000-0000-0000-000000000001"),
                        UUID.fromString("00000000-0000-0000-0000-000000000002"),
                        LocalDate.now().minusDays(Period.ofWeeks(0).getDays())
                )
        );
        tracks.add(
                new HabitTrackEntity(
                        UUID.fromString("00000000-0000-0000-0000-000000000002"),
                        UUID.fromString("00000000-0000-0000-0000-000000000002"),
                        LocalDate.now().minusDays(Period.ofWeeks(1).getDays())
                )
        );
        tracks.add(
                new HabitTrackEntity(
                        UUID.fromString("00000000-0000-0000-0000-000000000003"),
                        UUID.fromString("00000000-0000-0000-0000-000000000002"),
                        LocalDate.now().minusDays(Period.ofWeeks(2).getDays())
                )
        );
    }

    @Override
    public void create(CreateHabitTrackDto dto) {
        HabitTrackEntity habitTrackEntity = new HabitTrackEntity(UUID.randomUUID(), dto.getHabitId(), LocalDate.now());
        tracks.add(habitTrackEntity);
    }

    @Override
    public HabitTrack get(UUID id) {
        return null;
    }

    @Override
    public List<HabitTrack> getHabitTracks(UUID habitId) {
        List<HabitTrack> result = new ArrayList<>();
        for (var track : tracks) {
            if (track.getHabitId().equals(habitId)) {
                result.add(mapper.toDomain(track));
            }
        }
        return result;
    }

    @Override
    public void removeByHabitId(UUID habitId) {
        tracks.removeIf(track -> track.getHabitId().equals(habitId));
    }
}
