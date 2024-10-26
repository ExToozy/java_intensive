package org.example.infrastructure.web.servlets;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.core.exceptions.ConfigException;
import org.example.infrastructure.configs.DbConfig;
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
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HabitStatisticServletTest {
    private HabitStatisticServlet habitStatisticServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;
    private Connection connection;

    @BeforeEach
    void setUp() throws IOException, SQLException, ConfigException {
        habitStatisticServlet = new HabitStatisticServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        stringWriter = new StringWriter();
        PrintWriter printStream = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printStream);
        DbConfig dbConfig = new DbConfig();
        connection = DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUsername(), dbConfig.getPassword());
        MigrationTool.runMigrate();
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

        habitStatisticServlet.doGet(request, response);

        Map<String, Object> jsonResponse = JsonMapper.getJsonMap(new StringReader(stringWriter.toString()));
        assertThat(jsonResponse).containsEntry("status_code", 200);
        var jsonData = (Map<?, ?>) jsonResponse.get("data");
        assertThat(jsonData).isNotNull();
        var habitStatistics = (Map<?, ?>) jsonData.get("habitStatistics");
        assertThat(habitStatistics).isNotEmpty().hasSize(3);
    }
}