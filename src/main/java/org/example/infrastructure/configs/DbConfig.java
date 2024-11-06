package org.example.infrastructure.configs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@NoArgsConstructor
@Component
public class DbConfig {
    @Value("${db.driver}")
    private String driver;
    @Value("${db.changeLogFile}")
    private String changeLogFile;
    @Value("${db.url}")
    private String url;
    @Value("${db.username}")
    private String username;
    @Value("${db.password}")
    private String password;
    @Value("${db.defaultSchemaName}")
    private String defaultSchemaName;
    @Value("${db.liquibaseSchemaName}")
    private String liquibaseSchemaName;
}
