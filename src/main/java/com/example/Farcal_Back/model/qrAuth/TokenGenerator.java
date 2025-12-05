package com.example.Farcal_Back.model.qrAuth;

import java.security.SecureRandom;
import java.util.Base64;

public class TokenGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateSecureToken() {
        byte[] bytes = new byte[64]; // 64 bytes = 512 bits
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
