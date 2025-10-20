package com.example.Farcal_Back.model;

import org.springframework.data.annotation.Transient;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Classe utilitaire servant de clé composite logique
 * (utile pour référencer les calculs par date et id)
 */
public class CalculGlobalKey implements Serializable {

    private LocalDate dateCalcul;
    private UUID idCalcul;

    public CalculGlobalKey() {}

    public CalculGlobalKey(LocalDate dateCalcul, UUID idCalcul) {
        this.dateCalcul = dateCalcul;
        this.idCalcul = idCalcul;
    }

    public LocalDate getDateCalcul() {
        return dateCalcul;
    }

    public void setDateCalcul(LocalDate dateCalcul) {
        this.dateCalcul = dateCalcul;
    }

    public UUID getIdCalcul() {
        return idCalcul;
    }

    public void setIdCalcul(UUID idCalcul) {
        this.idCalcul = idCalcul;
    }

    @Override
    public String toString() {
        return "CalculGlobalKey{" +
                "dateCalcul=" + dateCalcul +
                ", idCalcul=" + idCalcul +
                '}';
    }
}
