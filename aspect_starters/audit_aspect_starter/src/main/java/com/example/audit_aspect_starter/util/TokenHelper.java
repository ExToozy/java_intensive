package com.example.audit_aspect_starter.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Optional;

/**
 * Утилита для работы с токенами аутентификации.
 * Содержит методы для извлечения идентификатора пользователя из токена.
 */
@Component
public class TokenHelper {

    private final SecretKey key;

    public TokenHelper(@Value("${jwt.token.signing-key}") String key) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
    }

    /**
     * Извлекает идентификатор пользователя из токена.
     *
     * @param token токен, содержащий идентификатор пользователя
     * @return идентификатор пользователя
     */
    public Optional<Integer> getUserIdFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            Integer userId = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(jwtToken)
                    .getPayload()
                    .get("user_id", Integer.class);
            return Optional.of(userId);

        }
        return Optional.empty();
    }
}

