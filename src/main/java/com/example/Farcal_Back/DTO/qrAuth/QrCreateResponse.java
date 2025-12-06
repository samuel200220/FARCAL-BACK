package com.example.Farcal_Back.DTO.qrAuth;

public class QrCreateResponse {
    private String tokenId;

    public QrCreateResponse(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
}