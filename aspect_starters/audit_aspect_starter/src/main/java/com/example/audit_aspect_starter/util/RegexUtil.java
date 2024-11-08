package com.example.audit_aspect_starter.util;

import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@NoArgsConstructor
public class RegexUtil {
    private static final Pattern TOKEN_PATTERN = Pattern.compile("^Bearer \\d+$");


    public static boolean isInvalidToken(String token) {
        return !TOKEN_PATTERN.matcher(token).matches();
    }
}
