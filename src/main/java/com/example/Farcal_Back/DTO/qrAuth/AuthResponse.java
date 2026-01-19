package com.example.Farcal_Back.DTO.qrAuth;

import com.example.Farcal_Back.DTO.qrAuth.EntrepriseDTO;

public class AuthResponse {

    private String token;
    private EntrepriseDTO user;

    public AuthResponse(String token, EntrepriseDTO user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public EntrepriseDTO getUser() {
        return user;
    }
}
