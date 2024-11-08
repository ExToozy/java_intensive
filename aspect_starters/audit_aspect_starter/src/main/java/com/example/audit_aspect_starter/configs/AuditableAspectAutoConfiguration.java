package com.example.audit_aspect_starter.configs;

import com.example.audit_aspect_starter.aspects.AuditableAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.example")
public class AuditableAspectAutoConfiguration {
    @Bean
    public AuditableAspect auditableAspect() {
        return new AuditableAspect();
    }
}
