package org.example.infrastructure.data.mappers;

import org.example.core.dtos.habit_dtos.CreateHabitDto;
import org.example.core.dtos.habit_dtos.UpdateHabitDto;
import org.example.core.models.Habit;
import org.example.infrastructure.data.models.HabitEntity;
import org.example.infrastructure.util.MapperConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper(uses = MapperConverter.class)
public interface HabitMapper {
    HabitMapper INSTANCE = Mappers.getMapper(HabitMapper.class);

    Habit toDomain(HabitEntity habitEntity);

    HabitEntity toEntity(Habit domain);

    @Mapping(source = "jsonMap.name", target = "name")
    @Mapping(source = "jsonMap.description", target = "description")
    @Mapping(source = "jsonMap.frequency", target = "frequency", qualifiedByName = "frequencyStrToEnum")
    CreateHabitDto toCreateHabitDto(Map<String, Object> jsonMap, int userId);

    @Mapping(source = "jsonMap.habitId", target = "id")
    @Mapping(source = "jsonMap.name", target = "name")
    @Mapping(source = "jsonMap.description", target = "description")
    @Mapping(source = "jsonMap.frequency", target = "frequency", qualifiedByName = "frequencyStrToEnum")
    UpdateHabitDto toUpdateHabitDto(Map<String, Object> jsonMap);

    Map<Integer, Map<String, Integer>> toHabitStatisticsMapDto(Map<Habit, Map<String, Integer>> habitMap);
}
