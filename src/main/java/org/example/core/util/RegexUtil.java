package org.example.core.util;

import java.util.regex.Pattern;

public class RegexUtil {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    private static final Pattern TOKEN_PATTERN = Pattern.compile("^Bearer \\d+$");

    private RegexUtil() {
    }

    public static boolean isInvalidEmail(String email) {
        return email.length() > 255 || !EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isInvalidToken(String token) {
        return !TOKEN_PATTERN.matcher(token).matches();
    }
}
