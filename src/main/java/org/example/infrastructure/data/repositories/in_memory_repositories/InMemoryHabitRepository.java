package org.example.infrastructure.data.repositories.in_memory_repositories;

import org.example.core.dtos.habit_dtos.CreateHabitDto;
import org.example.core.dtos.habit_dtos.UpdateHabitDto;
import org.example.core.models.Habit;
import org.example.core.models.HabitFrequency;
import org.example.core.repositories.IHabitRepository;
import org.example.infrastructure.data.mappers.HabitMapper;
import org.example.infrastructure.data.models.HabitEntity;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InMemoryHabitRepository implements IHabitRepository {
    private final List<HabitEntity> habits = new ArrayList<>();
    private final HabitMapper mapper = HabitMapper.INSTANCE;

    public InMemoryHabitRepository() {
        habits.addAll(Arrays.asList(
                        new HabitEntity(
                                1,
                                1,
                                "Drink water",
                                "Need to drink 2 liters of water every day",
                                HabitFrequency.DAILY,
                                LocalDate.now()
                        ),
                        new HabitEntity(
                                2,
                                1,
                                "Walk 10000 steps",
                                "Need to walk at least 10000 steps every day",
                                HabitFrequency.DAILY,
                                LocalDate.now()
                        ),
                        new HabitEntity(
                                3,
                                1,
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
        int nextIndex = habits.stream().mapToInt(HabitEntity::getId).max().orElse(1);
        HabitEntity habit = new HabitEntity(
                nextIndex,
                dto.userId(),
                dto.name(),
                dto.description(),
                dto.frequency(),
                LocalDate.now()
        );
        habits.add(habit);
    }


    @Override
    public List<Habit> getAllHabitsByUserId(int userId) {
        return habits.stream()
                .filter(habit -> habit.getUserId() == userId).map(mapper::toDomain)
                .toList();
    }

    @Override
    public void update(UpdateHabitDto dto) {
        for (var habit : habits) {
            if (habit.getId() == dto.id()) {
                habit.setDescription(dto.description());
                habit.setFrequency(dto.frequency());
                habit.setName(dto.name());
                break;
            }
        }
    }

    @Override
    public void remove(int id) {
        habits.removeIf(habitEntity -> habitEntity.getId() == id);
    }
}
