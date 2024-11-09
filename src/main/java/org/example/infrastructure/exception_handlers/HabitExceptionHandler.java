package org.example.infrastructure.exception_handlers;

import org.example.exceptions.HabitNotFoundException;
import org.example.infrastructure.constants.ErrorMessageConstants;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Order(0)
@RestControllerAdvice
public class HabitExceptionHandler {
    @ExceptionHandler(HabitNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleHabitNotFoundException(HabitNotFoundException e) {
        return Map.of("error", ErrorMessageConstants.HABIT_NOT_FOUND);
    }
}
