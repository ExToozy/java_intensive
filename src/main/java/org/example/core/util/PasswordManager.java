package org.example.core.util;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@NoArgsConstructor
@Slf4j
public class PasswordManager {
    private static final String SALT = "mpDhaJZfpFZBEOovhD6z2g==";


    public static boolean checkPasswordEquals(String password, String userPassword) {
        String passwordHash = getPasswordHash(password);
        return userPassword.equals(passwordHash);
    }

    public static String getPasswordHash(String password) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), Base64.getDecoder().decode(SALT), 65536, 128);
        String passwordHash = null;
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            passwordHash = Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("Error occurred while trying hash password", e);
        }
        return passwordHash;
    }
}
