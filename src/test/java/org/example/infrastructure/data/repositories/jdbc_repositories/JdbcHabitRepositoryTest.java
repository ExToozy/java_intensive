package org.example.infrastructure.data.repositories.jdbc_repositories;

import org.example.core.models.HabitFrequency;
import org.example.core.repositories.habit_repository.dtos.CreateHabitDto;
import org.example.core.repositories.habit_repository.dtos.UpdateHabitDto;
import org.example.infrastructure.configs.DbConfig;
import org.example.infrastructure.migration.MigrationTool;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcHabitRepositoryTest {

    private JdbcHabitRepository habitRepository;
    private Connection connection;

    @BeforeEach
    void setUp() throws IOException, SQLException {
        DbConfig dbConfig = new DbConfig();
        connection = DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUsername(), dbConfig.getUsername());
        MigrationTool.runMigrate();
        habitRepository = new JdbcHabitRepository();
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void create_shouldCreateRowInDatabase() {
        habitRepository.create(new CreateHabitDto(1, "name", "desc", HabitFrequency.DAILY));
        assertThat(habitRepository.getAllHabitsByUserId(1))
                .isNotNull()
                .isNotEmpty()
                .hasSize(4);

    }

    @Test
    void getAllHabitsByUserId_shouldGetAllUserRowsFromDatabase() {
        assertThat(habitRepository.getAllHabitsByUserId(1))
                .isNotNull()
                .isNotEmpty()
                .hasSize(3);
    }

    @Test
    void update() {
        habitRepository.update(new UpdateHabitDto(1, "newName", "newDesc", HabitFrequency.WEEKLY));

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