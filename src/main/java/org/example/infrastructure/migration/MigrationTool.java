package org.example.infrastructure.migration;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.infrastructure.configs.DbConfig;
import org.example.infrastructure.util.ConnectionManager;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
@RequiredArgsConstructor
@Slf4j
public class MigrationTool {
    private final DbConfig dbConfig;
    private final ConnectionManager connectionManager;

    public void runMigrate() {
        try (Connection connection = connectionManager.open()) {
            createSchemas(connection, dbConfig);
            migrateDatabase(connection, dbConfig);
        } catch (SQLException | LiquibaseException e) {
            log.error("Error occurred while trying migrate database", e);
        }
    }

    private void createSchemas(Connection connection, DbConfig dbConfig) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE SCHEMA IF NOT EXISTS " + dbConfig.getLiquibaseSchemaName());
            statement.executeUpdate("CREATE SCHEMA IF NOT EXISTS " + dbConfig.getDefaultSchemaName());
        }
    }

    private void migrateDatabase(Connection connection, DbConfig dbConfig) throws LiquibaseException {
        Database database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(connection));
        Liquibase liquibase = new Liquibase(dbConfig.getChangeLogFile(), new ClassLoaderResourceAccessor(), database);
        database.setLiquibaseSchemaName(dbConfig.getLiquibaseSchemaName());
        database.setDefaultSchemaName(dbConfig.getDefaultSchemaName());
        liquibase.update();
        liquibase.close();
    }
}
