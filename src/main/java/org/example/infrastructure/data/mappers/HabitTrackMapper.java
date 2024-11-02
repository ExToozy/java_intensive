package org.example.infrastructure.data.mappers;

import org.example.core.dtos.habit_track_dtos.CreateHabitTrackDto;
import org.example.core.models.HabitTrack;
import org.example.infrastructure.data.models.HabitTrackEntity;
import org.example.infrastructure.util.MapperConverter;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper(uses = MapperConverter.class)
public interface HabitTrackMapper {
    HabitTrackMapper INSTANCE = Mappers.getMapper(HabitTrackMapper.class);

    HabitTrack toDomain(HabitTrackEntity habitTrackEntity);

    HabitTrackEntity toEntity(HabitTrack domain);

    CreateHabitTrackDto toCreateHabitTrackDto(Map<String, Object> jsonMap);
}
