package com.example.audit_aspect_starter.util;

import java.util.Optional;

/**
 * Утилита для работы с токенами аутентификации.
 * Содержит методы для проверки валидности токена и извлечения идентификатора пользователя из токена.
 */
public class TokenHelper {

    /**
     * Проверяет, является ли токен валидным.
     *
     * @param token токен для проверки
     * @return true, если токен валиден; иначе false
     */
    private static boolean isValidToken(String token) {
        return token != null && !token.isBlank() && !RegexUtil.isInvalidToken(token);
    }

    /**
     * Извлекает идентификатор пользователя из токена.
     *
     * @param token токен, содержащий идентификатор пользователя
     * @return идентификатор пользователя
     */
    public static Optional<Integer> getUserIdFromToken(String token) {
        if (isValidToken(token)) {
            return Optional.of(Integer.parseInt(token.split(" ")[1]));
        }
        return Optional.empty();
    }
}

