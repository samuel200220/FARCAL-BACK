package com.example.Farcal_Back.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "Objet contribution")
@Table("contributions")
public class Contribution {

    @Id
    private UUID id;

    @Column("user_id")
    private UUID userId;

    @Column("depart_osm")
    private String departOsm;

    @Column("destination_osm")
    private String destinationOsm;

    @Column("distance_km")
    private double distanceKm;

    @Column("prix_paye")
    private int prixPaye;

    @Column("heure")
    private String heure;

    @Column("jour_semaine")
    private String jourSemaine;

    @Column("jour_ferie")
    private boolean jourFerie;

    @Column("pluie")
    private boolean pluie;

    @Column("etat_route")
    private String etatRoute;

    @Column("routes_travaux")
    private boolean routesTravaux;

    @Column("accident")
    private boolean accident;

    @Column("bagages")
    private boolean bagages;

    @Column("routes_larges")
    private boolean routesLarges;

    @Column("timestamp")
    private Instant timestamp;

    // getters & setters


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getDepartOsm() {
        return departOsm;
    }

    public void setDepartOsm(String departOsm) {
        this.departOsm = departOsm;
    }

    public String getDestinationOsm() {
        return destinationOsm;
    }

    public void setDestinationOsm(String destinationOsm) {
        this.destinationOsm = destinationOsm;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public int getPrixPaye() {
        return prixPaye;
    }

    public void setPrixPaye(int prixPaye) {
        this.prixPaye = prixPaye;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public String getJourSemaine() {
        return jourSemaine;
    }

    public void setJourSemaine(String jourSemaine) {
        this.jourSemaine = jourSemaine;
    }

    public boolean isJourFerie() {
        return jourFerie;
    }

    public void setJourFerie(boolean jourFerie) {
        this.jourFerie = jourFerie;
    }

    public boolean isPluie() {
        return pluie;
    }

    public void setPluie(boolean pluie) {
        this.pluie = pluie;
    }

    public String getEtatRoute() {
        return etatRoute;
    }

    public void setEtatRoute(String etatRoute) {
        this.etatRoute = etatRoute;
    }

    public boolean isRoutesTravaux() {
        return routesTravaux;
    }

    public void setRoutesTravaux(boolean routesTravaux) {
        this.routesTravaux = routesTravaux;
    }

    public boolean isAccident() {
        return accident;
    }

    public void setAccident(boolean accident) {
        this.accident = accident;
    }

    public boolean isBagages() {
        return bagages;
    }

    public void setBagages(boolean bagages) {
        this.bagages = bagages;
    }

    public boolean isRoutesLarges() {
        return routesLarges;
    }

    public void setRoutesLarges(boolean routesLarges) {
        this.routesLarges = routesLarges;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
