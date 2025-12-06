package com.example.Farcal_Back.DTO.qrAuth;

import java.util.UUID;

public class QrApproveRequest {
    private String tokenId;
    private UUID userId;

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}