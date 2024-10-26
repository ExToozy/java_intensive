package org.example.infrastructure.web.servlets;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.core.exceptions.ConfigException;
import org.example.core.exceptions.UserNotFoundException;
import org.example.core.models.User;
import org.example.core.util.PasswordManager;
import org.example.infrastructure.configs.DbConfig;
import org.example.infrastructure.data.repositories.jdbc_repositories.JdbcUserRepository;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServletTest {
    private UserServlet userServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;
    private JdbcUserRepository userRepository;
    private Connection connection;

    @BeforeEach
    void setUp() throws IOException, SQLException, ConfigException {
        userServlet = new UserServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        stringWriter = new StringWriter();
        PrintWriter printStream = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printStream);
        DbConfig dbConfig = new DbConfig();
        connection = DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUsername(), dbConfig.getPassword());
        MigrationTool.runMigrate();
        userRepository = new JdbcUserRepository();
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    @DisplayName("doGet should return list of users and return OK status")
    void doGet_shouldReturnListOfUsersAndReturnOk() throws Exception {
        int adminUserId = 2;
        String authHeader = "Bearer " + adminUserId;

        when(request.getHeader("Authorization")).thenReturn(authHeader);

        userServlet.doGet(request, response);

        Map<String, Object> jsonResponse = JsonMapper.getJsonMap(new BufferedReader(new StringReader(stringWriter.toString())));
        assertThat(jsonResponse).containsEntry("status_code", 200);

        var jsonData = (Map<?, ?>) jsonResponse.get("data");
        assertThat(jsonData).isNotNull();
        var users = (List<?>) jsonData.get("users");
        assertThat(users).isNotNull().isNotEmpty().hasSize(3);
    }

    @Test
    @DisplayName("doPut should update user and return OK status")
    void doPut_shouldChangeUserAdminStatusAndReturnOk() throws Exception {
        int userId = 1;
        String authHeader = "Bearer " + userId;
        String json = """
                {
                    "userId": 1,
                    "email": "newMail@test.ru",
                    "password": "1234"
                }""";

        BufferedReader jsonReader = new BufferedReader(new StringReader(json));
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(request.getReader()).thenReturn(jsonReader);

        userServlet.doPut(request, response);

        Map<String, Object> jsonResponse = JsonMapper.getJsonMap(new BufferedReader(new StringReader(stringWriter.toString())));
        assertThat(jsonResponse).containsEntry("status_code", 200);

        User updatedUser = userRepository.getById(userId);
        assertThat(updatedUser.getEmail()).isEqualTo("newMail@test.ru");
        assertThat(updatedUser.getPassword()).isEqualTo(PasswordManager.getPasswordHash("1234"));
    }

    @Test
    @DisplayName("doPost should update user admin status and return OK status")
    void doPost_shouldUpdateUserAdminStatusAndReturnOk() throws Exception {
        int adminUserId = 2;
        int targetUserId = 1;
        String authHeader = "Bearer " + adminUserId;
        String json = """
                {
                    "userId": 1,
                    "isAdmin": true
                }""";

        BufferedReader jsonReader = new BufferedReader(new StringReader(json));
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(request.getReader()).thenReturn(jsonReader);

        userServlet.doPost(request, response);

        Map<String, Object> jsonResponse = JsonMapper.getJsonMap(new StringReader(stringWriter.toString()));
        assertThat(jsonResponse).containsEntry("status_code", 200);

        User updatedUser = userRepository.getById(targetUserId);
        assertThat(updatedUser.isAdmin()).isTrue();
    }

    @Test
    @DisplayName("doDelete should remove user and return No Content status")
    void doDelete_shouldRemoveUserAndReturnNoContent() throws Exception {
        int userId = 2;
        String authHeader = "Bearer " + userId;
        String json = """
                {
                    "userId": 2
                }""";

        BufferedReader jsonReader = new BufferedReader(new StringReader(json));
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(request.getReader()).thenReturn(jsonReader);

        userServlet.doDelete(request, response);

        Map<String, Object> jsonResponse = JsonMapper.getJsonMap(new BufferedReader(new StringReader(stringWriter.toString())));
        assertThat(jsonResponse).containsEntry("status_code", 204);

        assertThatThrownBy(() -> userRepository.getById(userId)).isInstanceOf(UserNotFoundException.class);
    }
}