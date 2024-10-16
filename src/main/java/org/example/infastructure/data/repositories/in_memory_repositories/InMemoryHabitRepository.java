package org.example.infastructure.data.repositories.in_memory_repositories;

import org.example.core.models.Habit;
import org.example.core.models.HabitFrequency;
import org.example.core.repositories.habit_repository.IHabitRepository;
import org.example.core.repositories.habit_repository.dtos.CreateHabitDto;
import org.example.core.repositories.habit_repository.dtos.UpdateHabitDto;
import org.example.infastructure.data.mappers.HabitMapper;
import org.example.infastructure.data.mappers.Mapper;
import org.example.infastructure.data.models.HabitEntity;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class InMemoryHabitRepository implements IHabitRepository {
    private final List<HabitEntity> habits = new ArrayList<>();
    private final Mapper<Habit, HabitEntity> mapper = new HabitMapper();

    public InMemoryHabitRepository() {
        UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000000");
        habits.addAll(Arrays.asList(
                        new HabitEntity(
                                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                                userId,
                                "Drink water",
                                "Need to drink 2 liters of water every day",
                                HabitFrequency.DAILY,
                                LocalDate.now()
                        ),
                        new HabitEntity(
                                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                                userId,
                                "Walk 10000 steps",
                                "Need to walk at least 10000 steps every day",
                                HabitFrequency.DAILY,
                                LocalDate.now()
                        ),
                        new HabitEntity(
                                UUID.fromString("00000000-0000-0000-0000-000000000002"),
                                userId,
                                "Training",
                                "Go to the gym once a week",
                                HabitFrequency.WEEKLY,
                                LocalDate.now().minusDays(Period.ofWeeks(3).getDays())
                        )
                )
        );
    }

    @Override
    public void create(CreateHabitDto dto) {
        HabitEntity habit = new HabitEntity(
                UUID.randomUUID(),
                dto.getUserId(),
                dto.getName(),
                dto.getDescription(),
                dto.getFrequency(),
                LocalDate.now()
        );
        habits.add(habit);
    }


    @Override
    public List<Habit> getAllHabitsByUserId(UUID userId) {
        return habits.stream()
                .filter(habit -> habit.getUserID().equals(userId)).map(mapper::toDomain)
                .toList();
    }

    @Override
    public void update(UpdateHabitDto dto) {
        for (var habit : habits) {
            if (habit.getId().equals(dto.getId())) {
                habit.setDescription(dto.getDescription());
                habit.setFrequency(dto.getFrequency());
                habit.setName(dto.getName());
                break;
            }
        }
    }

    @Override
    public void remove(UUID id) {
        habits.removeIf(habitEntity -> habitEntity.getId().equals(id));
    }
}
