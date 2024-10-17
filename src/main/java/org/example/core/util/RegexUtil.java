package org.example.core.util;

import java.util.regex.Pattern;

public class RegexUtil {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    private RegexUtil() {
    }

    public static boolean isInvalidEmail(String email) {
        return email.length() > 255 || !EMAIL_PATTERN.matcher(email).matches();
    }
}
