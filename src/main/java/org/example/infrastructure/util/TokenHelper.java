package org.example.infrastructure.util;

import org.example.core.util.RegexUtil;
import org.example.exceptions.InvalidTokenException;

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
    public static int getUserIdFromToken(String token) throws InvalidTokenException {
        if (isValidToken(token)) {
            return Integer.parseInt(token.split(" ")[1]);
        }
        throw new InvalidTokenException();
    }
}
