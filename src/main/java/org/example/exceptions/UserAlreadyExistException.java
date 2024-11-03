package org.example.exceptions;

/**
 * Исключение обозначающие, что пользователь уже существует
 */
public class UserAlreadyExistException extends Exception {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}