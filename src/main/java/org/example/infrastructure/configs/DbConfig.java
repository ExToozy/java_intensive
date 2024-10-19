package org.example.infrastructure.configs;

import java.io.IOException;

public class DbConfig {
    private String changeLogFile;
    private String url;
    private String username;
    private String password;
    private String defaultSchemaName;
    private String liquibaseSchemaName;

    public DbConfig() throws IOException {
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("db/dbConfig.properties"));
        changeLogFile = System.getProperty("changeLogFile");
        url = System.getProperty("url");
        username = System.getProperty("username");
        password = System.getProperty("password");
        defaultSchemaName = System.getProperty("defaultSchemaName");
        liquibaseSchemaName = System.getProperty("liquibaseSchemaName");
    }

    public String getChangeLogFile() {
        return changeLogFile;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDefaultSchemaName() {
        return defaultSchemaName;
    }

    public String getLiquibaseSchemaName() {
        return liquibaseSchemaName;
    }
}
