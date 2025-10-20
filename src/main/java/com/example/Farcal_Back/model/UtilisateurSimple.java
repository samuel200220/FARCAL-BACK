package com.example.Farcal_Back.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

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

    public UtilisateurSimple() {}

    public UtilisateurSimple(UUID id, String nom, String email, Instant dateCreation) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.dateCreation = dateCreation;
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
}
