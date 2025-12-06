package com.example.Farcal_Back.service.qrAuth;


import com.example.Farcal_Back.model.qrAuth.QrAuthToken;
import com.example.Farcal_Back.model.qrAuth.TokenGenerator;
import com.example.Farcal_Back.repository.qrAuth.QrAuthRepositoryRedis;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class QrAuthService {

    private final QrAuthRepositoryRedis redisRepo;
    private final HmacService hmacService;

    public QrAuthService(QrAuthRepositoryRedis redisRepo, HmacService hmacService) {
        this.redisRepo = redisRepo;
        this.hmacService = hmacService;
    }

    public String createToken(String sessionId) {

        String tokenId = TokenGenerator.generateSecureToken();
        String hmac = hmacService.generate(tokenId, sessionId);

        QrAuthToken token = new QrAuthToken();
        token.setId(tokenId);
        token.setCreatedAt(LocalDateTime.now());
        token.setSessionId(sessionId);
        token.setHmac(hmac);

        redisRepo.save(token, 350); // expire auto après 5 minutes 50 secondes

        return tokenId;
    }

    public QrAuthToken getToken(String id) {
        QrAuthToken token = redisRepo.find(id);

        if (token == null) {
            throw new RuntimeException("Token not found or expired");
        }
        return token;
    }

    public void validateToken(String id, UUID userId){
        QrAuthToken token = getToken(id);
        token.setUserId(userId);
        token.setValidated(true);

        redisRepo.save(token, 180); // encore 3 minutes de vie
    }

    public void linkSession(String tokenId, String sessionId) {
        QrAuthToken token = getToken(tokenId);
        token.setSessionId(sessionId);

        // Mettre à jour le HMAC avec la nouvelle session
        String newHmac = hmacService.generate(tokenId, sessionId);
        token.setHmac(newHmac);

        redisRepo.save(token, 300); // renouveler pour 5 minutes
    }

}