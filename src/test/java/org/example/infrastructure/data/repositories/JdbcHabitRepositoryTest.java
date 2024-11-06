package org.example.infrastructure.data.repositories;

import org.example.core.dtos.habit_dtos.CreateHabitDto;
import org.example.core.dtos.habit_dtos.UpdateHabitDto;
import org.example.core.models.HabitFrequency;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(value = "classpath:application.yml")
@Sql(value = {"classpath:test_sql_scripts/remove-all-data.sql", "classpath:test_sql_scripts/insert-test-data.sql"})
class JdbcHabitRepositoryTest {

    @Autowired
    private JdbcHabitRepository habitRepository;

    @Test
    @DisplayName("Should create a row in the database")
    void create_shouldCreateRowInDatabase() {
        habitRepository.create(new CreateHabitDto(1, "name", "desc", "DAILY"));
        assertThat(habitRepository.getAllHabitsByUserId(1))
                .isNotNull()
                .isNotEmpty()
                .hasSize(4);
    }

    @Test
    @DisplayName("Should get all user rows from the database")
    void getAllHabitsByUserId_shouldGetAllUserRowsFromDatabase() {
        assertThat(habitRepository.getAllHabitsByUserId(1))
                .isNotNull()
                .isNotEmpty()
                .hasSize(3);
    }

    @Test
    @DisplayName("Should update an existing habit")
    void update() {
        habitRepository.update(1, new UpdateHabitDto("newName", "newDesc", "WEEKLY"));

        var updatedHabit = habitRepository
                .getAllHabitsByUserId(1)
                .stream()
                .filter(habit -> habit.getId() == 1)
                .findFirst()
                .orElse(null);

        assertThat(updatedHabit).isNotNull();
        assertThat(updatedHabit.getName()).isEqualTo("newName");
        assertThat(updatedHabit.getDescription()).isEqualTo("newDesc");
        assertThat(updatedHabit.getFrequency()).isEqualTo(HabitFrequency.WEEKLY);
    }

    @Test
    @DisplayName("Should remove an existing habit")
    void remove() {
        int habitIdToRemove = 2;
        habitRepository.remove(habitIdToRemove);

        var removedHabit = habitRepository
                .getAllHabitsByUserId(1)
                .stream()
                .filter(habit -> habit.getId() == habitIdToRemove)
                .findFirst()
                .orElse(null);

        assertThat(removedHabit).isNull();
    }
}
