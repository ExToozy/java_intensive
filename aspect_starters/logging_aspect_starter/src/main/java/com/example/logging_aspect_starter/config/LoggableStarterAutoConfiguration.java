package com.example.logging_aspect_starter.config;


import com.example.logging_aspect_starter.aspects.LoggableAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggableStarterAutoConfiguration {
    @Bean
    public LoggableAspect loggableAspect() {
        return new LoggableAspect();
    }
}
