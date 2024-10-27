package org.example.infrastructure.web.servlets;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.core.exceptions.ConfigException;
import org.example.core.exceptions.UserNotFoundException;
import org.example.infrastructure.configs.DbConfig;
import org.example.infrastructure.data.repositories.jdbc_repositories.JdbcUserRepository;
import org.example.infrastructure.migration.MigrationTool;
import org.example.infrastructure.util.JsonMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegisterServletTest {

    private RegisterServlet registerServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;
    private JdbcUserRepository userRepository;
    private Connection connection;

    @BeforeEach
    void setUp() throws IOException, SQLException, ConfigException {
        registerServlet = new RegisterServlet();
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
    @DisplayName("doPost should return token if user creds is correct and create user")
    void doPost_shouldReturnToken_whenUserProvideCorrectCreds() throws IOException, UserNotFoundException {
        String json = """
                {
                    "email": "test@test.ru",
                    "password": "123"
                }""";

        BufferedReader jsonReader = new BufferedReader(new StringReader(json));
        when(request.getReader()).thenReturn(jsonReader);

        registerServlet.doPost(request, response);

        Map<String, Object> jsonResponse = JsonMapper.getJsonMap(new StringReader(stringWriter.toString()));
        assertThat(jsonResponse).containsEntry("status_code", 201);
        var jsonData = (Map<?, ?>) jsonResponse.get("data");
        assertThat(jsonData).isNotNull();
        var userToken = Integer.parseInt((String) jsonData.get("token"));
        assertThat(userToken).isEqualTo(4);

        assertThat(userRepository.getById(4)).isNotNull();
    }
}