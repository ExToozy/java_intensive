package com.example.logging_aspect_starter.annotations;


import com.example.logging_aspect_starter.config.LoggableStarterAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(LoggableStarterAutoConfiguration.class)
public @interface EnableLoggingAspect {
}
