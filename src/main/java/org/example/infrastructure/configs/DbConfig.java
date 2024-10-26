package org.example.infrastructure.configs;

import org.example.core.exceptions.ConfigException;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

public class DbConfig {
    private final String driver;
    private final String changeLogFile;
    private final String url;
    private final String username;
    private final String password;
    private final String defaultSchemaName;
    private final String liquibaseSchemaName;

    public DbConfig() throws ConfigException {
        loadProperties();
        changeLogFile = System.getProperty("changeLogFile");
        url = System.getProperty("url");
        username = System.getProperty("username");
        password = System.getProperty("password");
        defaultSchemaName = System.getProperty("defaultSchemaName");
        liquibaseSchemaName = System.getProperty("liquibaseSchemaName");
        driver = System.getProperty("driver");
    }

    private void loadProperties() throws ConfigException {
        URL resourcesPath = this.getClass().getClassLoader().getResource("db");
        String dbPropertiesPath = resourcesPath.getPath() + "/dbConfig.properties";
        try {
            System.getProperties().load(new FileReader(dbPropertiesPath));
        } catch (IOException e) {
            throw new ConfigException("Properties could not be loaded");
        }
    }

    public String getChangeLogFile() {
        return changeLogFile;
    }

    public String getUrl() {
        return url;
    }


    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }


    public String getDefaultSchemaName() {
        return defaultSchemaName;
    }

    public String getLiquibaseSchemaName() {
        return liquibaseSchemaName;
    }

    public String getDriver() {
        return driver;
    }
}
