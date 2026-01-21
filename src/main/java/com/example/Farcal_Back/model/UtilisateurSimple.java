package com.example.Farcal_Back.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Objet Utilisateur Standard")
@Table("utilisateur_simple")
public class UtilisateurSimple {

    @Id
    private UUID id;

    @Column("nom")
    private String nom;

    @Column("email")
    private String email;

    @Column("date_creation")
    private Instant dateCreation;

    @Column("email_verifie")
    private Boolean emailVerifie = false;

    @Column("code_verification")
    private String codeVerification;

    @Column("expiration_code")
    private LocalDateTime expirationCode;

    @Column("provider")
    private String provider = "local"; // "local" ou "google"

    public UtilisateurSimple() {}

    public UtilisateurSimple(UUID id, String nom, String email, Instant dateCreation) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.dateCreation = dateCreation;
        this.emailVerifie = false;
        this.provider = "local";
    }

    // Getters et Setters existants...

    public Boolean getEmailVerifie() {
        return emailVerifie;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setEmailVerifie(Boolean emailVerifie) {
        this.emailVerifie = emailVerifie;
    }

    public String getCodeVerification() {
        return codeVerification;
    }

    public void setCodeVerification(String codeVerification) {
        this.codeVerification = codeVerification;
    }

    public LocalDateTime getExpirationCode() {
        return expirationCode;
    }

    public void setExpirationCode(LocalDateTime expirationCode) {
        this.expirationCode = expirationCode;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}