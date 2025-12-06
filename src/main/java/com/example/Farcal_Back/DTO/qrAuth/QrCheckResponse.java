package com.example.Farcal_Back.DTO.qrAuth;

import java.util.UUID;

public class QrCheckResponse {
    private boolean validated;
    private UUID userId; // si validé, sinon null

    public QrCheckResponse(boolean validated, UUID userId) {
        this.validated = validated;
        this.userId = userId;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}