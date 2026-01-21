package com.example.Farcal_Back.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

@Schema(description = "Objet Calcul")
@Table("calculs_utilisateur")
public class CalculUtilisateur {

    @Id
    private UUID id;

    @Column("id_utilisateur")
    private UUID idUtilisateur;

    @Column("timestamp")
    private Instant timestamp;

    @Column("lieu_depart")
    @JsonProperty("lieuDepart")
    private String lieuDepart;

    @Column("lieu_arrivee")
    @JsonProperty("lieuArrivee")
    private String lieuArrivee;

    @Column("heure_prise_en_charge")
    @JsonProperty("heurePriseEnCharge")
    private LocalTime heurePriseEnCharge;

    @Column("distance_km")
    @JsonProperty("distanceKm")
    private double distanceKm;

    @Column("cout_estime")
    @JsonProperty("coutEstime")
    private BigDecimal coutEstime;

    @Column("tarif_officiel")
    @JsonProperty("tarifOfficiel")
    private BigDecimal tarifOfficiel;

    // Nouveaux champs pour les paramètres de calcul
    @Column("jour_semaine")
    @JsonProperty("jourSemaine")
    private String jourSemaine;

    @Column("jour_ferie")
    @JsonProperty("jourFerie")
    private String jourFerie;

    @Column("pluie")
    @JsonProperty("pluie")
    private String pluie;

    @Column("etat_route")
    @JsonProperty("etatRoute")
    private String etatRoute;

    @Column("accident")
    @JsonProperty("accident")
    private String accident;

    @Column("bagages")
    @JsonProperty("bagages")
    private String bagages;

    @Column("routes_larges")
    @JsonProperty("routesLarges")
    private String routesLarges;

    @Column("routes_travaux")
    @JsonProperty("routesTravaux")
    private String routesTravaux;

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

    public String getJourSemaine() { return jourSemaine; }
    public void setJourSemaine(String jourSemaine) { this.jourSemaine = jourSemaine; }

    public String getJourFerie() { return jourFerie; }
    public void setJourFerie(String jourFerie) { this.jourFerie = jourFerie; }

    public String getPluie() { return pluie; }
    public void setPluie(String pluie) { this.pluie = pluie; }

    public String getEtatRoute() { return etatRoute; }
    public void setEtatRoute(String etatRoute) { this.etatRoute = etatRoute; }

    public String getAccident() { return accident; }
    public void setAccident(String accident) { this.accident = accident; }

    public String getBagages() { return bagages; }
    public void setBagages(String bagages) { this.bagages = bagages; }

    public String getRoutesLarges() { return routesLarges; }
    public void setRoutesLarges(String routesLarges) { this.routesLarges = routesLarges; }

    public String getRoutesTravaux() { return routesTravaux; }
    public void setRoutesTravaux(String routesTravaux) { this.routesTravaux = routesTravaux; }
}