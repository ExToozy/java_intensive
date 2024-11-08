package com.example.audit_aspect_starter.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component("auditDbConfig")
@ConfigurationProperties(prefix = "spring.datasource")
public class DbConfig {
    private String username;
    private String password;
    private String url;
}
