package com.example.Farcal_Back.controller;

import com.example.Farcal_Back.model.UtilisateurSimple;
import com.example.Farcal_Back.repository.UtilisateurSimpleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurSimpleController {

    private static final Logger logger = LoggerFactory.getLogger(UtilisateurSimpleController.class);
    private final UtilisateurSimpleRepository repository;

    public UtilisateurSimpleController(UtilisateurSimpleRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Mono<ResponseEntity<UtilisateurSimple>> create(@RequestBody UtilisateurSimple user) {
        logger.info("Création d'un nouvel utilisateur: {}", user.getEmail());

        // Ne pas définir l'ID manuellement, laisser R2DBC le gérer
        user.setDateCreation(Instant.now());

        return repository.save(user)
                .doOnSuccess(savedUser -> logger.info("Utilisateur créé avec succès: {}", savedUser.getId()))
                .doOnError(error -> logger.error("Erreur lors de la création de l'utilisateur", error))
                .map(savedUser -> ResponseEntity.status(HttpStatus.CREATED).body(savedUser))
                .onErrorResume(error ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build())
                );
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UtilisateurSimple>> getById(@PathVariable UUID id) {
        logger.info("Récupération de l'utilisateur par ID: {}", id);

        return repository.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public Mono<ResponseEntity<UtilisateurSimple>> getByEmail(@PathVariable("email") String email) {
        logger.info("Récupération de l'utilisateur par email: {}", email);

        return repository.findByEmail(email)
                .map(user -> {
                    logger.info("Utilisateur trouvé: {}", user.getId());
                    return ResponseEntity.ok(user);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable UUID id) {
        logger.info("Suppression de l'utilisateur: {}", id);

        return repository.deleteById(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorResume(error -> {
                    logger.error("Erreur lors de la suppression de l'utilisateur", error);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    // Ajoutez cette méthode dans UtilisateurSimpleController
//    @PostMapping("/check-email")
//    public Mono<ResponseEntity<Map<String, Object>>> checkEmailExists(@RequestBody Map<String, String> request) {
//        String email = request.get("email");
//        logger.info("Vérification de l'existence de l'email: {}", email);
//
//        return repository.existsByEmail(email)
//                .map(exists -> ResponseEntity.ok(Map.of("exists", exists)))
//                .defaultIfEmpty(ResponseEntity.ok(Map.of("exists", false)));
//    }

    // Endpoint de santé pour vérifier si l'API fonctionne
    @GetMapping("/health")
    public Mono<ResponseEntity<String>> health() {
        return Mono.just(ResponseEntity.ok("API is running"));
    }
}