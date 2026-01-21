package com.example.Farcal_Back.controller;

import com.example.Farcal_Back.model.UtilisateurSimple;
import com.example.Farcal_Back.repository.UtilisateurSimpleRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.oauth2.jwt.Jwt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import com.example.Farcal_Back.service.JwtService;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/utilisateurs")
@Tag(name = "AUTH_USER", description = "CRUD D'UN UTILISATEUR STANDARD")
public class UtilisateurSimpleController {

    private static final Logger logger = LoggerFactory.getLogger(UtilisateurSimpleController.class);
    private final UtilisateurSimpleRepository repository;
    private final JwtService jwtService;

    public UtilisateurSimpleController(UtilisateurSimpleRepository repository, JwtService jwtService) {
        this.repository = repository;
        this.jwtService = jwtService;
    }

    @Operation(
            summary = "Inscription d'un utilisateur standard",
            description = "Enregistre un utilisateur dans la base de donnees"
    )
    @PostMapping("/register") // Renomme pour clarté
    public Mono<ResponseEntity<Map<String, Object>>> register(@RequestBody UtilisateurSimple user) {
        logger.info("Inscription d'un nouvel utilisateur: {}", user.getEmail());

        user.setDateCreation(Instant.now());
        // TODO: Génére et envoie code de vérification par email (implémente si pas déjà fait)

        return repository.save(user)
                .flatMap(savedUser -> {
                    // Token temporaire (après vérif réelle, tu pourras le conditionner à emailVerifie == true)
                    String token = jwtService.generateToken(savedUser.getId(), savedUser.getEmail());
                    Map<String, Object> response = Map.of(
                            "token", token,
                            "user", savedUser,
                            "id", savedUser.getId()
                    );
                    return Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(response));
                })
                .onErrorResume(error -> Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Email existe déjà"))));
    }

    @Operation(summary = "Obtenir l'identifiant d'un utilisateur")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UtilisateurSimple>> getById(@PathVariable UUID id) {
        logger.info("Récupération de l'utilisateur par ID: {}", id);

        return repository.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtenir un utilisateur par son email")
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

    @Operation(summary = "Obtenir les informations d'un utilisateur")
    @GetMapping("/me")
    public Mono<ResponseEntity<UtilisateurSimple>> me(
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());

        return repository.findById(userId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @Operation(summary = "Suppression d'un utilisateur")
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
    @Operation(summary = "Endpoint de santé pour vérifier si l'API fonctionne")
    @GetMapping("/health")
    public Mono<ResponseEntity<String>> health() {
        return Mono.just(ResponseEntity.ok("API is running"));
    }
}