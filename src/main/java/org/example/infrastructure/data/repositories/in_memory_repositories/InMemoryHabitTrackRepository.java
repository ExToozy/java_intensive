package org.example.infrastructure.data.repositories.in_memory_repositories;

import org.example.core.dtos.habit_track_dtos.CreateHabitTrackDto;
import org.example.core.models.HabitTrack;
import org.example.core.repositories.IHabitTrackRepository;
import org.example.infrastructure.data.mappers.HabitTrackMapper;
import org.example.infrastructure.data.models.HabitTrackEntity;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InMemoryHabitTrackRepository implements IHabitTrackRepository {
    private final List<HabitTrackEntity> tracks = new ArrayList<>();
    private final HabitTrackMapper mapper = HabitTrackMapper.INSTANCE;

    public InMemoryHabitTrackRepository() {
        tracks.addAll(Arrays.asList(
                        new HabitTrackEntity(
                                1,
                                1,
                                LocalDate.now()
                        ),
                        new HabitTrackEntity(
                                2,
                                3,
                                LocalDate.now().minusDays(Period.ofWeeks(0).getDays())
                        ),
                        new HabitTrackEntity(
                                3,
                                3,
                                LocalDate.now().minusDays(Period.ofWeeks(1).getDays())
                        ),
                        new HabitTrackEntity(
                                4,
                                3
                                ,
                                LocalDate.now().minusDays(Period.ofWeeks(2).getDays())
                        )
                )
        );
    }

    @Override
    public void create(CreateHabitTrackDto dto) {
        int nextIndex = tracks.stream().mapToInt(HabitTrackEntity::getId).max().orElse(1);
        HabitTrackEntity habitTrackEntity = new HabitTrackEntity(nextIndex, dto.getHabitId(), LocalDate.now());
        tracks.add(habitTrackEntity);
    }

    @Override
    public List<HabitTrack> getHabitTracks(int habitId) {
        List<HabitTrack> result = new ArrayList<>();
        for (var track : tracks) {
            if (track.getHabitId() == habitId) {
                result.add(mapper.toDomain(track));
            }
        }
        return result;
    }

    @Override
    public void removeAllByHabitId(int habitId) {
        tracks.removeIf(track -> track.getHabitId() == habitId);
    }

    @Override
    public void remove(int id) {
        tracks.removeIf(track -> track.getId() == id);
    }
}
