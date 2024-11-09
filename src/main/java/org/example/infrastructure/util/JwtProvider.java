package org.example.infrastructure.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.example.core.models.User;
import org.example.exceptions.InvalidTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Утилита для работы с токенами аутентификации.
 * Содержит методы для проверки валидности токена и извлечения идентификатора пользователя из токена.
 */
@Component
@Slf4j
public class JwtProvider {
    private final SecretKey jwtSecret;

    public JwtProvider(@Value("${jwt.token.signing-key}") String jwtSecret) {
        this.jwtSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    /**
     * Проверяет, является ли токен валидным.
     *
     * @param token токен для проверки
     * @return true, если токен валиден; иначе false
     */
    private boolean isValidToken(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                String jwtToken = token.substring(7);
                Jwts.parser()
                        .verifyWith(jwtSecret)
                        .build()
                        .parseSignedClaims(jwtToken);
                return true;
            }
            return false;
        } catch (JwtException e) {
            log.warn(e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Token error", e);
        }

        return false;
    }

    /**
     * Генерирует токен для пользователя.
     *
     * @param user {@link User} пользователь для которого нужно сгенерировать токен
     * @return идентификатор пользователя
     */
    public String generateAccessToken(User user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .subject(user.getEmail())
                .expiration(accessExpiration)
                .signWith(jwtSecret)
                .claim("user_id", user.getId())
                .claim("is_admin", user.isAdmin())
                .compact();

    }

    /**
     * Извлекает идентификатор пользователя из токена.
     *
     * @param token токен, содержащий идентификатор пользователя
     * @return идентификатор пользователя
     */
    public int getUserIdFromToken(String token) throws InvalidTokenException {
        if (isValidToken(token)) {
            String jwtToken = token.substring(7);
            return getClaims(jwtToken).get("user_id", Integer.class);
        }
        throw new InvalidTokenException();
    }


    /**
     * Получает данные пользователя из токена.
     *
     * @param token токен пользователя
     * @return идентификатор пользователя
     */
    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtSecret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
