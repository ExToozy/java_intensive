package org.example.core.repositories.habit_repository;


import org.example.core.models.Habit;
import org.example.core.repositories.habit_repository.dtos.CreateHabitDto;
import org.example.core.repositories.habit_repository.dtos.UpdateHabitDto;

import java.util.List;
import java.util.UUID;

public interface IHabitRepository {
    void create(CreateHabitDto dto);

    List<Habit> getAllHabitsByUserId(UUID userId);

    void update(UpdateHabitDto dto);

    void remove(UUID id);
}
