package com.example.Farcal_Back.controller;

import com.example.Farcal_Back.DTO.qrAuth.ContributionRequest;
import com.example.Farcal_Back.model.Contribution;
import com.example.Farcal_Back.repository.ContributionRepository;
import com.example.Farcal_Back.repository.UtilisateurSimpleRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/contributions")
@Tag(name = "CONTRIBUTIONS", description = "ENDPOINT POUR LES CONTRIBUTIONS")
public class ContributionController {

    private static final Logger logger =
            LoggerFactory.getLogger(ContributionController.class);

    private final ContributionRepository repository;
    private final UtilisateurSimpleRepository utilisateurRepository;

    public ContributionController(
            ContributionRepository repository,
            UtilisateurSimpleRepository utilisateurRepository
    ) {
        this.repository = repository;
        this.utilisateurRepository = utilisateurRepository;
    }

    /* ===================== CREATE ===================== */

    @Operation(summary = "Création d'une contribution")
    @PostMapping
    public Mono<ResponseEntity<Contribution>> saveContribution(
            @RequestBody ContributionRequest req,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String email = jwt.getClaim("email");
        logger.info("Recherche utilisateur avec email: '{}'", email);

        if (email == null) {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }

        return utilisateurRepository.findByEmail(email)
                .flatMap(user -> {
                    Contribution c = new Contribution();
                    c.setUserId(user.getId());

                    c.setDepartOsm(req.departOsm());
                    c.setDestinationOsm(req.destinationOsm());
                    c.setDistanceKm(req.distanceKm());
                    c.setPrixPaye(req.prixPaye());

                    c.setHeure(req.heure());
                    c.setJourSemaine(req.jourSemaine());
                    c.setJourFerie(req.jourFerie());

                    c.setPluie(req.pluie());
                    c.setEtatRoute(req.etatRoute());
                    c.setRoutesTravaux(req.routesTravaux());
                    c.setAccident(req.accident());
                    c.setBagages(req.bagages());
                    c.setRoutesLarges(req.routesLarges());

                    c.setTimestamp(Instant.now());

                    return repository.save(c);
                })
                .map(saved ->
                        ResponseEntity.status(HttpStatus.CREATED).body(saved)
                )
                .switchIfEmpty(
                        Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build())
                )
                .doOnError(e ->
                        logger.error("Erreur lors de l'enregistrement de la contribution", e)
                );
    }

    /* ===================== GET ALL ===================== */

    @Operation(summary = "Obtenir toutes les contributions")
    @GetMapping
    public Flux<Contribution> getAllContributions(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaim("email");

        if (email == null) {
            return Flux.empty();
        }

        return utilisateurRepository.findByEmail(email)
                .flatMapMany(user ->
                        repository.findByUserId(user.getId())
                )
                .doOnError(e ->
                        logger.error("Erreur lors de la récupération des contributions", e)
                );
    }

    /* ===================== GET ONE ===================== */

    @Operation(summary = "Obtenir toutes les contributions d'un utilisateur")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Contribution>> getContributionById(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String email = jwt.getClaim("email");

        if (email == null) {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }

        return utilisateurRepository.findByEmail(email)
                .flatMap(user -> repository.findById(id)
                        .map(contribution -> {
                            // Vérifier que la contribution appartient à l'utilisateur
                            if (!contribution.getUserId().equals(user.getId())) {
                                return ResponseEntity.status(HttpStatus.FORBIDDEN).<Contribution>build();
                            }
                            return ResponseEntity.ok(contribution);
                        })
                        .defaultIfEmpty(ResponseEntity.notFound().build())
                )
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    /* ===================== HISTORY (ALIAS FOR GET ALL) ===================== */

    @GetMapping("/me")
    public Flux<Contribution> getMyHistory(@AuthenticationPrincipal Jwt jwt) {
        return getAllContributions(jwt);
    }

    /* ===================== UPDATE ===================== */

    @Operation(summary = "Update une contribution d'un utilisateur")
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Object>> updateContribution(
            @PathVariable UUID id,
            @RequestBody ContributionRequest req,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String email = jwt.getClaim("email");

        if (email == null) {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }

        return utilisateurRepository.findByEmail(email)
                .flatMap(user -> repository.findById(id)
                        .flatMap(existing -> {
                            // Vérifier que la contribution appartient à l'utilisateur
                            if (!existing.getUserId().equals(user.getId())) {
                                return Mono.just(ResponseEntity.<Contribution>status(HttpStatus.FORBIDDEN).build());
                            }

                            // Mettre à jour les champs
                            existing.setDepartOsm(req.departOsm());
                            existing.setDestinationOsm(req.destinationOsm());
                            existing.setDistanceKm(req.distanceKm());
                            existing.setPrixPaye(req.prixPaye());

                            existing.setHeure(req.heure());
                            existing.setJourSemaine(req.jourSemaine());
                            existing.setJourFerie(req.jourFerie());

                            existing.setPluie(req.pluie());
                            existing.setEtatRoute(req.etatRoute());
                            existing.setRoutesTravaux(req.routesTravaux());
                            existing.setAccident(req.accident());
                            existing.setBagages(req.bagages());
                            existing.setRoutesLarges(req.routesLarges());

                            existing.setTimestamp(Instant.now()); // Mettre à jour le timestamp

                            return repository.save(existing);
                        })
                        .map(updated -> ResponseEntity.ok(updated))
                        .defaultIfEmpty(ResponseEntity.notFound().build())
                )
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build())
                .doOnError(e ->
                        logger.error("Erreur lors de la mise à jour de la contribution", e)
                );
    }

    /* ===================== DELETE ===================== */

    @Operation(summary = "Suppression d'une contribution d'un utilisateur")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<?>> deleteContribution(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String email = jwt.getClaim("email");

        if (email == null) {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }

        return utilisateurRepository.findByEmail(email)
                .flatMap(user -> repository.findById(id)
                        .flatMap(existing -> {
                            // Vérifier que la contribution appartient à l'utilisateur
                            if (!existing.getUserId().equals(user.getId())) {
                                return Mono.just(ResponseEntity.<Void>status(HttpStatus.FORBIDDEN).build());
                            }

                            return repository.delete(existing)
                                    .then(Mono.just(ResponseEntity.noContent().<Void>build()));
                        })
                        .defaultIfEmpty(ResponseEntity.notFound().build())
                )
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build())
                .doOnError(e ->
                        logger.error("Erreur lors de la suppression de la contribution", e)
                );
    }
}