package org.example.infrastructure.data.mappers;

import org.example.core.models.HabitTrack;
import org.example.infrastructure.data.models.HabitTrackEntity;

public class HabitTrackMapper implements Mapper<HabitTrack, HabitTrackEntity> {
    @Override
    public HabitTrack toDomain(HabitTrackEntity habitTrackEntity) {
        return new HabitTrack(
                habitTrackEntity.getId(),
                habitTrackEntity.getHabitId(),
                habitTrackEntity.getCompleteDate()
        );
    }

    @Override
    public HabitTrackEntity toEntity(HabitTrack domain) {
        return new HabitTrackEntity(
                domain.getId(),
                domain.getHabitId(),
                domain.getCompleteDate()
        );
    }
}
