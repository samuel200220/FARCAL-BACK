package com.example.Farcal_Back.service.qrAuth;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class HmacService {

    @Value("${security.hmac.secret}")
    private String secretKey;

    public String generate(String tokenId, String sessionId) {
        try {
            String data = tokenId + ":" + sessionId;

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secretKeySpec);

            byte[] rawHmac = sha256_HMAC.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(rawHmac);

        } catch (Exception e) {
            throw new RuntimeException("Erreur génération HMAC", e);
        }
    }

    public boolean verify(String tokenId, String sessionId, String hmacToVerify) {
        String expectedHmac = generate(tokenId, sessionId);
        return expectedHmac.equals(hmacToVerify);
    }
}

