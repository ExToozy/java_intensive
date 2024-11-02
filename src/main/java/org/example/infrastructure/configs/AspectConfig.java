package org.example.infrastructure.configs;

import org.aspectj.lang.Aspects;
import org.example.aspects.AuditableAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AspectConfig {

    @Bean
    AuditableAspect auditableAspect() {
        return Aspects.aspectOf(AuditableAspect.class);
    }
}
