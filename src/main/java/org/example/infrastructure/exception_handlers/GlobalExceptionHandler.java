package org.example.infrastructure.exception_handlers;

import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.InvalidTokenException;
import org.example.infrastructure.constants.ErrorMessageConstants;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Order(1)
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            if (!errors.containsKey(error.getField())) {
                errors.put(error.getField(), new ArrayList<>());
            }
            errors.get(error.getField()).add(error.getDefaultMessage());
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Map<String, String> handleMethodNotAllowedException(HttpRequestMethodNotSupportedException ex) {
        return Map.of("error", ErrorMessageConstants.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(NoHandlerFoundException ex) {
        return Map.of("error", ErrorMessageConstants.UNKNOWN_ENDPOINT);
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, String> handleInvalidTokenException(InvalidTokenException ex) {
        return Map.of("error", ErrorMessageConstants.USER_UNAUTHORIZED_OR_TOKEN_INVALID);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, String> handleMissingRequestHeaderException(MissingRequestHeaderException ex) throws MissingRequestHeaderException {
        if (ex.getHeaderName().equals("Authorization")) {
            return Map.of("error", ErrorMessageConstants.USER_UNAUTHORIZED_OR_TOKEN_INVALID);
        }
        throw new MissingRequestHeaderException(ex.getHeaderName(), ex.getParameter());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, String> handleAccessDeniedException(AccessDeniedException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleAnotherExceptions(Exception ex) {
        log.error("Internal server error", ex);
        return Map.of("error", ErrorMessageConstants.INTERVAL_SERVER_ERROR);
    }
}
