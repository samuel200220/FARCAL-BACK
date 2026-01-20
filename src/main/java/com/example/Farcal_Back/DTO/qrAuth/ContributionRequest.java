package com.example.Farcal_Back.DTO.qrAuth;

public record ContributionRequest(
        String departOsm,
        String destinationOsm,
        double distanceKm,
        int prixPaye,

        String heure,
        String jourSemaine,
        boolean jourFerie,

        boolean pluie,
        String etatRoute,
        boolean routesTravaux,
        boolean accident,
        boolean bagages,
        boolean routesLarges
) {}

