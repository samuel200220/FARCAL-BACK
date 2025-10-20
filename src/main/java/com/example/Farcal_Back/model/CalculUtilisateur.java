package com.example.Farcal_Back.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

@Table("calculs_utilisateur")
public class CalculUtilisateur {

    @Id
    private UUID id; // ✅ Généré côté Java avant insertion

    @Column("id_utilisateur")
    private UUID idUtilisateur;

    @Column("timestamp")
    private Instant timestamp;

    @Column("lieu_depart")
    @JsonProperty("lieu_depart")
    private String lieuDepart;

    @Column("lieu_arrivee")
    @JsonProperty("lieu_arrivee")
    private String lieuArrivee;

    @Column("heure_prise_en_charge")
    @JsonProperty("heure_prise_en_charge")
    private LocalTime heurePriseEnCharge;

    @Column("distance_km")
    @JsonProperty("distance_km")
    private double distanceKm;

    @Column("cout_estime")
    @JsonProperty("cout_estime")
    private BigDecimal coutEstime;

    @Column("tarif_officiel")
    @JsonProperty("tarif_officiel")
    private BigDecimal tarifOfficiel;

    // --- Getters / Setters ---
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getIdUtilisateur() { return idUtilisateur; }
    public void setIdUtilisateur(UUID idUtilisateur) { this.idUtilisateur = idUtilisateur; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public String getLieuDepart() { return lieuDepart; }
    public void setLieuDepart(String lieuDepart) { this.lieuDepart = lieuDepart; }

    public String getLieuArrivee() { return lieuArrivee; }
    public void setLieuArrivee(String lieuArrivee) { this.lieuArrivee = lieuArrivee; }

    public LocalTime getHeurePriseEnCharge() { return heurePriseEnCharge; }
    public void setHeurePriseEnCharge(LocalTime heurePriseEnCharge) { this.heurePriseEnCharge = heurePriseEnCharge; }

    public double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }

    public BigDecimal getCoutEstime() { return coutEstime; }
    public void setCoutEstime(BigDecimal coutEstime) { this.coutEstime = coutEstime; }

    public BigDecimal getTarifOfficiel() { return tarifOfficiel; }
    public void setTarifOfficiel(BigDecimal tarifOfficiel) { this.tarifOfficiel = tarifOfficiel; }
}
