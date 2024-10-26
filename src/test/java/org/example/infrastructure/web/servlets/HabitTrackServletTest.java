package org.example.infrastructure.web.servlets;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.core.exceptions.ConfigException;
import org.example.core.models.HabitTrack;
import org.example.infrastructure.configs.DbConfig;
import org.example.infrastructure.data.repositories.jdbc_repositories.JdbcHabitTrackRepository;
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
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HabitTrackServletTest {
    private HabitTrackServlet habitTrackServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;
    private JdbcHabitTrackRepository habitTrackRepository;
    private Connection connection;

    @BeforeEach
    void setUp() throws IOException, SQLException, ConfigException {
        habitTrackServlet = new HabitTrackServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        stringWriter = new StringWriter();
        PrintWriter printStream = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printStream);
        DbConfig dbConfig = new DbConfig();
        connection = DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUsername(), dbConfig.getPassword());
        MigrationTool.runMigrate();
        habitTrackRepository = new JdbcHabitTrackRepository();
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    @DisplayName("doPost should create a habit track and return created status")
    void doPost_shouldCreateHabitAndReturnCreated() throws Exception {
        int userId = 1;
        String authHeader = "Bearer " + userId;
        String json = """
                {
                    "habitId": 2
                }""";
        BufferedReader jsonReader = new BufferedReader(new StringReader(json));
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(request.getReader()).thenReturn(jsonReader);

        habitTrackServlet.doPost(request, response);

        Map<String, Object> jsonResponse = JsonMapper.getJsonMap(new StringReader(stringWriter.toString()));
        assertThat(jsonResponse).containsEntry("status_code", 201);

        List<HabitTrack> habitTracks = habitTrackRepository.getHabitTracks(2);
        assertThat(habitTracks).isNotNull().isNotEmpty().hasSize(1);
    }

    @Test
    @DisplayName("doGet should return habit tracks and return created status")
    void doGet_shouldReturnUserHabits() throws Exception {
        int userId = 1;
        String authHeader = "Bearer " + userId;

        when(request.getHeader("Authorization")).thenReturn(authHeader);

        habitTrackServlet.doGet(request, response);

        Map<String, Object> jsonResponse = JsonMapper.getJsonMap(new StringReader(stringWriter.toString()));
        assertThat(jsonResponse).containsEntry("status_code", 200);
        var jsonData = (Map<?, ?>) jsonResponse.get("data");
        assertThat(jsonData).isNotNull();
        var habits = (List<?>) jsonData.get("habitTracks");
        assertThat(habits).isNotNull().isNotEmpty().hasSize(4);
    }

    @Test
    @DisplayName("doDelete should remove a habit track and return No Content status")
    void doDelete_shouldRemoveHabitAndReturnNoContent() throws Exception {
        int userId = 1;
        String authHeader = "Bearer " + userId;
        String json = """
                {
                    "trackId": 1
                }""";
        BufferedReader jsonReader = new BufferedReader(new StringReader(json));
        when(request.getReader()).thenReturn(jsonReader);
        when(request.getHeader("Authorization")).thenReturn(authHeader);

        habitTrackServlet.doDelete(request, response);

        Map<String, Object> jsonResponse = JsonMapper.getJsonMap(new StringReader(stringWriter.toString()));
        assertThat(jsonResponse).containsEntry("status_code", 204);

        List<HabitTrack> habitTracks = habitTrackRepository.getHabitTracks(1);

        assertThat(habitTracks).isEmpty();
    }
}