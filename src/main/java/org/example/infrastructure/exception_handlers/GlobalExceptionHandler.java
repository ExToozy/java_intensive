package org.example.infrastructure.exception_handlers;

import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.InvalidTokenException;
import org.example.infrastructure.constants.ErrorMessageConstants;
import org.example.infrastructure.util.ErrorResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            if (!errors.containsKey(error.getField())) {
                errors.put(error.getField(), new ArrayList<>());
            }
            errors.get(error.getField()).add(error.getDefaultMessage());
        }

        return new ErrorResponse(errors);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse handleMethodNotAllowedException() {
        return new ErrorResponse(ErrorMessageConstants.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException() {
        return new ErrorResponse(ErrorMessageConstants.UNKNOWN_ENDPOINT);
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleInvalidTokenException() {
        return new ErrorResponse(ErrorMessageConstants.USER_UNAUTHORIZED_OR_TOKEN_INVALID);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleMissingRequestHeaderException(MissingRequestHeaderException ex) throws MissingRequestHeaderException {
        if (ex.getHeaderName().equals("Authorization")) {
            return new ErrorResponse(ErrorMessageConstants.USER_UNAUTHORIZED_OR_TOKEN_INVALID);
        }
        throw new MissingRequestHeaderException(ex.getHeaderName(), ex.getParameter());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException() {
        return new ErrorResponse(ErrorMessageConstants.JSON_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDeniedException(AccessDeniedException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAnotherExceptions(Exception ex) {
        log.error("Internal server error", ex);
        return new ErrorResponse(ErrorMessageConstants.INTERVAL_SERVER_ERROR);
    }
}
