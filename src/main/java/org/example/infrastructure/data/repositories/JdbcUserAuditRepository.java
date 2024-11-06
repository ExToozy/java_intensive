package org.example.infrastructure.data.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.core.dtos.user_audit_dtos.CreateUserAuditDto;
import org.example.infrastructure.constants.SqlConstants;
import org.example.infrastructure.util.ConnectionManager;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JdbcUserAuditRepository {
    private final ConnectionManager connectionManager;

    public void create(CreateUserAuditDto createUserAuditDto) {
        try (Connection connection = connectionManager.open()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlConstants.CREATE_USER_AUDIT_SQL);

            preparedStatement.setInt(1, createUserAuditDto.getUserId());
            preparedStatement.setString(2, createUserAuditDto.getRequestUri());
            preparedStatement.setString(3, createUserAuditDto.getRequestBody());
            preparedStatement.setString(4, createUserAuditDto.getResponseBody());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("Error occurred while trying create user audit", e);
        }
    }
}
