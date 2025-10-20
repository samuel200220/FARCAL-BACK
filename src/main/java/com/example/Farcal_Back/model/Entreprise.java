package com.example.Farcal_Back.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Table("entreprise")
public class Entreprise {

    @Id
    private UUID id;

    @Column("nom")
    private String nom;

    @Column("email")
    private String email;

    @Column("mot_de_passe")
    private String motDePasse;

    @Column("responsable")
    private String responsable;

    @Column("date_creation")
    private Instant dateCreation;

    public Entreprise() {}

    public Entreprise(UUID id, String nom, String email, String motDePasse,
                      String responsable, Instant dateCreation) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.responsable = responsable;
        this.dateCreation = dateCreation;
    }

    // Getters et Setters
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

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }
}