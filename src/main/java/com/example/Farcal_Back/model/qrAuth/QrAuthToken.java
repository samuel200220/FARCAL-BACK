package com.example.Farcal_Back.model.qrAuth;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class QrAuthToken implements Serializable {

    private String id;
    private String sessionId;  // UUID
    private UUID userId; // rempli après la validation mobile
    private boolean validated = false;
    private LocalDateTime createdAt;
    private String hmac;  // ← IMPORTANT pour HMAC

    public QrAuthToken() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getHmac() {
        return hmac;
    }

    public void setHmac(String hmac) {
        this.hmac = hmac;
    }
}