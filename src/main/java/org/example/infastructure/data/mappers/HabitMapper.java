package org.example.infastructure.data.mappers;

import org.example.core.models.Habit;
import org.example.infastructure.data.models.HabitEntity;

public class HabitMapper implements Mapper<Habit, HabitEntity> {
    @Override
    public Habit toDomain(HabitEntity habitEntity) {
        return new Habit(
                habitEntity.getId(),
                habitEntity.getUserID(),
                habitEntity.getName(),
                habitEntity.getDescription(),
                habitEntity.getFrequency(),
                habitEntity.getDayOfCreation()
        );
    }

    @Override
    public HabitEntity toEntity(Habit domain) {
        return new HabitEntity(
                domain.getId(),
                domain.getUserId(),
                domain.getName(),
                domain.getDescription(),
                domain.getFrequency(),
                domain.getDayOfCreation()
        );
    }
}
