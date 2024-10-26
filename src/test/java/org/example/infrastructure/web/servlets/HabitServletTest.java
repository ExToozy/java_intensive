package org.example.infrastructure.web.servlets;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.core.exceptions.ConfigException;
import org.example.core.models.Habit;
import org.example.core.models.HabitFrequency;
import org.example.infrastructure.configs.DbConfig;
import org.example.infrastructure.data.repositories.jdbc_repositories.JdbcHabitRepository;
import org.example.infrastructure.migration.MigrationTool;
import org.example.infrastructure.util.JsonMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HabitServletTest {

    private HabitServlet habitServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;
    private JdbcHabitRepository habitRepository;
    private Connection connection;

    @BeforeEach
    void setUp() throws IOException, SQLException, ConfigException {
        habitServlet = new HabitServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        stringWriter = new StringWriter();
        PrintWriter printStream = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printStream);
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
    @DisplayName("doPost should create a habit and return created status")
    void doPost_shouldCreateHabitAndReturnCreated() throws Exception {
        int userId = 1;
        String authHeader = "Bearer " + userId;
        String json = """
                {
                    "name": "testName",
                    "description": "testDescription",
                    "frequency": "DAILY"
                }""";
        BufferedReader jsonReader = new BufferedReader(new StringReader(json));
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(request.getReader()).thenReturn(jsonReader);

        habitServlet.doPost(request, response);

        Map<String, Object> jsonResponse = JsonMapper.getJsonMap(new StringReader(stringWriter.toString()));
        assertThat(jsonResponse).containsEntry("status_code", 201);

        List<Habit> userHabits = habitRepository.getAllHabitsByUserId(userId);
        assertThat(userHabits).anyMatch(habit -> habit.getName().equals("testName") &&
                habit.getDescription().equals("testDescription") &&
                habit.getFrequency() == HabitFrequency.DAILY &&
                habit.getDayOfCreation().equals(LocalDate.now()));
    }

    @Test
    @DisplayName("doGet should return user habits and return created status")
    void doGet_shouldReturnUserHabits() throws Exception {
        int userId = 1;
        String authHeader = "Bearer " + userId;

        when(request.getHeader("Authorization")).thenReturn(authHeader);

        habitServlet.doGet(request, response);

        Map<String, Object> jsonResponse = JsonMapper.getJsonMap(new StringReader(stringWriter.toString()));
        assertThat(jsonResponse).containsEntry("status_code", 200);
        var jsonData = (Map<?, ?>) jsonResponse.get("data");
        assertThat(jsonData).isNotNull();
        var habits = (List<?>) jsonData.get("habits");
        assertThat(habits).isNotNull().isNotEmpty().hasSize(3);
    }

    @Test
    @DisplayName("doPut should update a habit and return OK status")
    void doPut_shouldUpdateHabitAndReturnOk() throws Exception {
        int habitId = 1;
        int userId = 1;
        String authHeader = "Bearer " + userId;
        String json = """
                {
                    "habitId": 1,
                    "name": "updatedName",
                    "description": "updatedDescription",
                    "frequency": "WEEKLY"
                }""";

        BufferedReader jsonReader = new BufferedReader(new StringReader(json));
        when(request.getReader()).thenReturn(jsonReader);
        when(request.getHeader("Authorization")).thenReturn(authHeader);

        habitServlet.doPut(request, response);

        Map<String, Object> jsonResponse = JsonMapper.getJsonMap(new StringReader(stringWriter.toString()));
        assertThat(jsonResponse).containsEntry("status_code", 200);

        List<Habit> userHabits = habitRepository.getAllHabitsByUserId(userId);
        var updatedHabit = userHabits.stream().filter(habit -> habit.getId() == habitId).findFirst().orElse(null);
        assertThat(updatedHabit).isNotNull();
        assertThat(updatedHabit.getName()).isEqualTo("updatedName");
        assertThat(updatedHabit.getDescription()).isEqualTo("updatedDescription");
        assertThat(updatedHabit.getFrequency()).isEqualTo(HabitFrequency.WEEKLY);
    }

    @Test
    @DisplayName("doDelete should remove a habit and return No Content status")
    void doDelete_shouldRemoveHabitAndReturnNoContent() throws Exception {
        int habitId = 1;
        int userId = 1;
        String authHeader = "Bearer " + userId;
        String json = """
                {
                    "habitId": 1
                }""";
        BufferedReader jsonReader = new BufferedReader(new StringReader(json));
        when(request.getReader()).thenReturn(jsonReader);
        when(request.getHeader("Authorization")).thenReturn(authHeader);

        habitServlet.doDelete(request, response);

        Map<String, Object> jsonResponse = JsonMapper.getJsonMap(new StringReader(stringWriter.toString()));
        assertThat(jsonResponse).containsEntry("status_code", 204);

        List<Habit> userHabits = habitRepository.getAllHabitsByUserId(userId);
        var deletedHabit = userHabits.stream().filter(habit -> habit.getId() == habitId).findFirst().orElse(null);

        assertThat(deletedHabit).isNull();
    }
}