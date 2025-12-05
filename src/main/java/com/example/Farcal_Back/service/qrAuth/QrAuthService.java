package com.example.Farcal_Back.service.qrAuth;


import com.example.Farcal_Back.model.qrAuth.QrAuthToken;
import com.example.Farcal_Back.model.qrAuth.TokenGenerator;
import com.example.Farcal_Back.repository.qrAuth.QrAuthRepositoryRedis;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QrAuthService {

    private final QrAuthRepositoryRedis redisRepo;
    private final HmacService hmacService;

    public String createToken(String sessionId) {

        String tokenId = TokenGenerator.generateSecureToken();
        String hmac = hmacService.generate(tokenId, sessionId);

        QrAuthToken token = new QrAuthToken();
        token.setId(tokenId);
        token.setCreatedAt(LocalDateTime.now());
        token.setSessionId(sessionId);
        token.setHmac(hmac);

        redisRepo.save(token, 350); // expire auto après 2 minutes

        return tokenId;
    }

    public QrAuthToken getToken(String id) {
        QrAuthToken token = redisRepo.find(id);

        if (token == null) {
            throw new RuntimeException("Token not found or expired");
        }
        return token;
    }

    public void validateToken(String id, Long userId){
        QrAuthToken token = getToken(id);
        token.setUserId(userId);
        token.setValidated(true);

        redisRepo.save(token, 180); // encore 1 minute de vie
    }

    public void linkSession(String tokenId, String sessionId) {
        QrAuthToken token = getToken(tokenId);
        token.setSessionId(sessionId);

        redisRepo.save(token, 300);
    }

}

