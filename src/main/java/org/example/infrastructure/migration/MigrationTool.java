package org.example.infrastructure.migration;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.example.infrastructure.configs.DbConfig;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MigrationTool {
    public static void main(String[] args) throws IOException {
        runMigrate();
    }

    public static void runMigrate() throws IOException {
        DbConfig dbConfig = new DbConfig();
        System.out.println(String.format("%s %s %s", dbConfig.getUrl(), dbConfig.getUsername(), dbConfig.getPassword()));
        try (Connection connection = DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUsername(), dbConfig.getPassword())) {
            createSchemas(connection, dbConfig);
            migrateDatabase(connection, dbConfig);
        } catch (SQLException | LiquibaseException e) {
            System.out.println(new String(e.getMessage().getBytes(StandardCharsets.UTF_8)));
        }
    }

    private static void createSchemas(Connection connection, DbConfig dbConfig) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE SCHEMA IF NOT EXISTS " + dbConfig.getLiquibaseSchemaName());
            statement.executeUpdate("CREATE SCHEMA IF NOT EXISTS " + dbConfig.getDefaultSchemaName());
        }
    }

    private static void migrateDatabase(Connection connection, DbConfig dbConfig) throws LiquibaseException {
        Database database = DatabaseFactory
                .getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(connection));
        Liquibase liquibase = new Liquibase(
                dbConfig.getChangeLogFile(),
                new ClassLoaderResourceAccessor(),
                database
        );
        database.setLiquibaseSchemaName(dbConfig.getLiquibaseSchemaName());
        database.setDefaultSchemaName(dbConfig.getDefaultSchemaName());
        liquibase.update();
    }
}



