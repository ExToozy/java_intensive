package org.example.core.repositories.habit_repository;


import org.example.core.models.Habit;
import org.example.core.repositories.habit_repository.dtos.CreateHabitDto;
import org.example.core.repositories.habit_repository.dtos.UpdateHabitDto;

import java.util.List;
import java.util.UUID;

public interface IHabitRepository {
    void create(CreateHabitDto dto);

    Habit get(UUID id);

    List<Habit> getAll();

    List<Habit> getAllHabitsByUserId(UUID userId);


    void update(UpdateHabitDto dto);

    void remove(UUID id);
}
