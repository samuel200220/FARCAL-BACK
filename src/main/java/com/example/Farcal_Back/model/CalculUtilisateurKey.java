package com.example.Farcal_Back.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Cette classe n'est plus une clé composite comme en Cassandra.
 * Elle devient une entité simple si tu veux garder la notion de timestamp par utilisateur.
 */
@Table("calculs_utilisateur_keys")
public class CalculUtilisateurKey {

    @Id
    @Column("id")
    private Long id; // Clé primaire auto-générée dans PostgreSQL

    @Column("id_utilisateur")
    @JsonProperty("id_utilisateur")
    private UUID idUtilisateur;

    @Column("timestamp")
    @JsonProperty("timestamp")
    private Instant timestamp;

    public CalculUtilisateurKey() {}

    public CalculUtilisateurKey(UUID idUtilisateur, Instant timestamp) {
        this.idUtilisateur = idUtilisateur;
        this.timestamp = timestamp;
    }

    // --- Getters / Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(UUID idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
