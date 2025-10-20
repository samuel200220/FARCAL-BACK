package com.example.Farcal_Back.controller;

import com.example.Farcal_Back.model.Entreprise;
import com.example.Farcal_Back.repository.EntrepriseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/entreprises")
public class EntrepriseController {

    private static final Logger logger = LoggerFactory.getLogger(EntrepriseController.class);
    private final EntrepriseRepository repository;

    public EntrepriseController(EntrepriseRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Flux<Entreprise> all() {
        return repository.findAll();
    }

    @PostMapping
    public Mono<ResponseEntity<Entreprise>> create(@RequestBody Entreprise entreprise) {
        logger.info("Création d'une nouvelle entreprise: {}", entreprise.getEmail());

        // Laisser la base de données générer l'ID
        entreprise.setDateCreation(Instant.now());

        return repository.save(entreprise)
                .doOnSuccess(saved -> logger.info("Entreprise créée avec succès: {}", saved.getId()))
                .doOnError(error -> logger.error("Erreur lors de la création de l'entreprise", error))
                .map(saved -> ResponseEntity.status(HttpStatus.CREATED).body(saved))
                .onErrorResume(error ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build())
                );
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Entreprise>> getById(@PathVariable UUID id) {
        logger.info("Récupération de l'entreprise par ID: {}", id);

        return repository.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public Mono<ResponseEntity<Entreprise>> getByEmail(@PathVariable String email) {
        logger.info("Récupération de l'entreprise par email: {}", email);

        return repository.findByEmail(email)
                .map(entreprise -> {
                    logger.info("Entreprise trouvée: {}", entreprise.getId());
                    return ResponseEntity.ok(entreprise);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable UUID id) {
        logger.info("Suppression de l'entreprise: {}", id);

        return repository.deleteById(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorResume(error -> {
                    logger.error("Erreur lors de la suppression de l'entreprise", error);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<Entreprise>> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Tentative de connexion pour: {}", loginRequest.getEmail());

        return repository.findByEmail(loginRequest.getEmail())
                .flatMap(entreprise -> {
                    // Vérifier le mot de passe
                    if (entreprise.getMotDePasse().equals(loginRequest.getMotDePasse())) {
                        logger.info("Connexion réussie pour: {}", loginRequest.getEmail());
                        return Mono.just(ResponseEntity.ok(entreprise));
                    } else {
                        logger.warn("Mot de passe incorrect pour: {}", loginRequest.getEmail());
                        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).<Entreprise>build());
                    }
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/health")
    public Mono<ResponseEntity<String>> health() {
        return Mono.just(ResponseEntity.ok("Entreprise API is running"));
    }

    // Classe interne pour la requête de login
    public static class LoginRequest {
        private String email;
        private String motDePasse;

        public LoginRequest() {}

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
    }
}