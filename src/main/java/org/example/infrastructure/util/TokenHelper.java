package org.example.infrastructure.util;

import org.example.core.util.RegexUtil;

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
    public static boolean isValidToken(String token) {
        return token != null && !token.isBlank() && !RegexUtil.isInvalidToken(token);
    }

    /**
     * Извлекает идентификатор пользователя из токена.
     *
     * @param token токен, содержащий идентификатор пользователя
     * @return идентификатор пользователя
     */
    public static int getUserIdFromToken(String token) {
        return Integer.parseInt(token.split(" ")[1]);
    }
}
