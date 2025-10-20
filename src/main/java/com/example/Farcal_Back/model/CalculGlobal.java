package com.example.Farcal_Back.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Table("calculs_globaux")
public class CalculGlobal {

    @Id
    private Long id; // clé primaire auto-incrémentée dans PostgreSQL

    private LocalDate dateCalcul;
    private UUID utilisateurId;
    private String typeUtilisateur;
    private String lieuDepart;
    private String lieuArrivee;
    private LocalTime heurePriseEnCharge;
    private Double distanceKm;
    private BigDecimal coutEstime;
    private BigDecimal tarifOfficiel;
}
