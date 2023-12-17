package com.walkbuddies.backend.common;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class SHA256 {

    private static final int salt_length = 16;

    public static String createSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[salt_length];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String getEncrypt(String input, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((input + salt).getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
