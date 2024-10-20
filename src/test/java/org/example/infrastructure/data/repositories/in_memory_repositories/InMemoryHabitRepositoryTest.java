package org.example.infrastructure.data.repositories.in_memory_repositories;

import org.example.core.models.HabitFrequency;
import org.example.core.repositories.habit_repository.dtos.CreateHabitDto;
import org.example.core.repositories.habit_repository.dtos.UpdateHabitDto;
import org.example.infrastructure.data.models.HabitEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;


class InMemoryHabitRepositoryTest {

    InMemoryHabitRepository habitRepository;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        var userId = 0;
        var habits = new ArrayList<>(Arrays.asList(
                new HabitEntity(
                        0,
                        userId,
                        "testUser1HabitName",
                        "testUser1HabitDescription",
                        HabitFrequency.DAILY,
                        LocalDate.now()
                ),
                new HabitEntity(
                        1,
                        userId,
                        "testUser1HabitName",
                        "testUser1HabitDescription",
                        HabitFrequency.DAILY,
                        LocalDate.now()
                ),
                new HabitEntity(
                        2,
                        userId,
                        "testUser1HabitName",
                        "testUser1HabitDescription",
                        HabitFrequency.WEEKLY,
                        LocalDate.now().minusDays(Period.ofWeeks(3).getDays())
                ),
                new HabitEntity(
                        3,
                        1,
                        "testUser2HabitName",
                        "testUser2HabitDescription",
                        HabitFrequency.DAILY,
                        LocalDate.now().minusDays(1)
                )
        )
        );
        habitRepository = new InMemoryHabitRepository();
        Field field = InMemoryHabitRepository.class.getDeclaredField("habits");
        field.setAccessible(true);
        field.set(habitRepository, habits);
    }

    @DisplayName("Check that create habit add habit to memory")
    @Test
    void create_shouldAddHabitToMemory() throws IOException {
        habitRepository.create(
                new CreateHabitDto(
                        2,
                        "testName",
                        "testDescription",
                        HabitFrequency.DAILY
                )
        );

        var userHabits = habitRepository.getAllHabitsByUserId(2);
        assertThat(userHabits)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .allMatch(habit -> habit.getName().equals("testName") &&
                        habit.getDescription().equals("testDescription") &&
                        habit.getFrequency().equals(HabitFrequency.DAILY) &&
                        habit.getDayOfCreation().equals(LocalDate.now())
                );
    }

    @DisplayName("Check getAllHabitsByUserId return correct habits")
    @Test
    void getAllHabitsByUserId_shouldReturnUserHabits() {
        var habitsUser1 = habitRepository.getAllHabitsByUserId(0);
        var habitsUser2 = habitRepository.getAllHabitsByUserId(1);

        assertThat(habitsUser1).isNotNull().isNotEmpty().hasSize(3);
        assertThat(habitsUser2).isNotNull().isNotEmpty().hasSize(1);

    }

    @DisplayName("Check that remove really remove habit from memory")
    @Test
    void remove_shouldRemoveHabitFromMemory() {
        habitRepository.remove(3);
        var habitsUser2 = habitRepository.getAllHabitsByUserId(1);

        assertThat(habitsUser2).isNotNull().isEmpty();
    }

    @DisplayName("Check that update habit working correctly")
    @Test
    void update() {
        habitRepository.update(
                new UpdateHabitDto(
                        3,
                        "newHabitName",
                        "newHabitDescription",
                        HabitFrequency.WEEKLY
                )
        );
        var updatedHabit = habitRepository
                .getAllHabitsByUserId(1)
                .get(0);

        assertThat(updatedHabit.getId()).isEqualTo(3);
        assertThat(updatedHabit.getName()).isEqualTo("newHabitName");
        assertThat(updatedHabit.getDescription()).isEqualTo("newHabitDescription");
        assertThat(updatedHabit.getFrequency()).isEqualTo(HabitFrequency.WEEKLY);
        assertThat(updatedHabit.getDayOfCreation()).isEqualTo(LocalDate.now().minusDays(1));
    }
}