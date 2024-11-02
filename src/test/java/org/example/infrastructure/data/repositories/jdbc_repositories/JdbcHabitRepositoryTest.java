package org.example.infrastructure.data.repositories.jdbc_repositories;

import org.example.core.dtos.habit_dtos.CreateHabitDto;
import org.example.core.dtos.habit_dtos.UpdateHabitDto;
import org.example.core.exceptions.ConfigException;
import org.example.core.models.HabitFrequency;
import org.example.infrastructure.configs.DbConfig;
import org.example.infrastructure.migration.MigrationTool;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcHabitRepositoryTest {

    private JdbcHabitRepository habitRepository;
    private Connection connection;

    @BeforeEach
    void setUp() throws ConfigException, SQLException {
        DbConfig dbConfig = new DbConfig();
        connection = DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUsername(), dbConfig.getPassword());
        MigrationTool.runMigrate();
        habitRepository = new JdbcHabitRepository();
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    @DisplayName("Should create a row in the database")
    void create_shouldCreateRowInDatabase() {
        habitRepository.create(new CreateHabitDto(1, "name", "desc", HabitFrequency.DAILY));
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
