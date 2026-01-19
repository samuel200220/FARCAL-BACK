package com.example.Farcal_Back.DTO.qrAuth;

import com.example.Farcal_Back.model.Entreprise;
import java.time.Instant;
import java.util.UUID;

public class EntrepriseDTO {

    private UUID id;
    private String nom;
    private String email;
    private String responsable;
    private Instant dateCreation;

    public static EntrepriseDTO fromEntity(Entreprise e) {
        EntrepriseDTO dto = new EntrepriseDTO();
        dto.id = e.getId();
        dto.nom = e.getNom();
        dto.email = e.getEmail();
        dto.responsable = e.getResponsable();
        dto.dateCreation = e.getDateCreation();
        return dto;
    }

    // getters

    public UUID getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public String getResponsable() {
        return responsable;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }
}
