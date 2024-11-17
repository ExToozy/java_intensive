package org.example.infrastructure.exception_handlers;

import org.example.exceptions.HabitTrackNotFoundException;
import org.example.infrastructure.constants.ErrorMessageConstants;
import org.example.infrastructure.util.ErrorResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(0)
@RestControllerAdvice
public class HabitTrackExceptionHandler {
    @ExceptionHandler(HabitTrackNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleHabitTrackNotFoundException() {
        return new ErrorResponse(ErrorMessageConstants.HABIT_TRACK_NOT_FOUND);
    }
}
