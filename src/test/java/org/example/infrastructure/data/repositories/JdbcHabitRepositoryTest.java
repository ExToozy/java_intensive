package org.example.infrastructure.data.repositories;

import org.example.core.dtos.habit_dtos.CreateHabitDto;
import org.example.core.dtos.habit_dtos.UpdateHabitDto;
import org.example.core.models.HabitFrequency;
import org.example.infrastructure.configs.DbConfig;
import org.example.infrastructure.migration.MigrationTool;
import org.example.infrastructure.util.ConnectionManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DbConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class JdbcHabitRepositoryTest {

    @Autowired
    private DbConfig dbConfig;
    private JdbcHabitRepository habitRepository;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        ConnectionManager connectionManager = new ConnectionManager(dbConfig);
        connection = connectionManager.open();
        new MigrationTool(dbConfig, connectionManager).runMigrate();
        habitRepository = new JdbcHabitRepository(connectionManager);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

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
