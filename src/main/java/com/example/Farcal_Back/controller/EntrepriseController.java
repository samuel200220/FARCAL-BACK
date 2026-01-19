package com.example.Farcal_Back.controller;

import com.example.Farcal_Back.DTO.qrAuth.AuthResponse;
import com.example.Farcal_Back.DTO.qrAuth.EntrepriseDTO;
import com.example.Farcal_Back.model.Entreprise;
import com.example.Farcal_Back.repository.EntrepriseRepository;
import com.example.Farcal_Back.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
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
    private final JwtService jwtService;

    public EntrepriseController(EntrepriseRepository repository, JwtService jwtService) {
        this.repository = repository;
        this.jwtService = jwtService;
    }

    /* ===================== CRUD ===================== */

    @GetMapping
    public Flux<EntrepriseDTO> all() {
        return repository.findAll()
                .map(EntrepriseDTO::fromEntity);
    }

    @PostMapping
    public Mono<ResponseEntity<EntrepriseDTO>> create(@RequestBody Entreprise entreprise) {
        entreprise.setDateCreation(Instant.now());

        return repository.save(entreprise)
                .map(saved ->
                        ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(EntrepriseDTO.fromEntity(saved))
                );
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Entreprise>> getById(@PathVariable UUID id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable UUID id) {
        return repository.deleteById(id)
                .thenReturn(ResponseEntity.noContent().build());
    }

    /* ===================== AUTH ===================== */

    @PostMapping("/login")
    public Mono<ResponseEntity<?>> login(@RequestBody LoginRequest request) {

        return repository.findByEmail(request.getEmail())
                .flatMap(entreprise -> {

                    if (!entreprise.getMotDePasse().equals(request.getMotDePasse())) {
                        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
                    }

                    String token = jwtService.generateToken(
                            entreprise.getId(),
                            entreprise.getEmail()
                    );

                    return Mono.just(
                            ResponseEntity.ok(
                                    new AuthResponse(token, EntrepriseDTO.fromEntity(entreprise))
                            )
                    );
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    /* ===================== CURRENT USER ===================== */

    @GetMapping("/me")
    public Mono<ResponseEntity<EntrepriseDTO>> me(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader
    ) {
        try {
            String token = authHeader.replace("Bearer ", "");
            UUID userId = jwtService.extractUserId(token);

            return repository.findById(userId)
                    .map(e -> ResponseEntity.ok(EntrepriseDTO.fromEntity(e)))
                    .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());

        } catch (Exception e) {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }
    }

    /* ===================== HEALTH ===================== */

    @GetMapping("/health")
    public Mono<ResponseEntity<String>> health() {
        return Mono.just(ResponseEntity.ok("Entreprise API is running"));
    }

    /* ===================== DTO ===================== */

    public static class LoginRequest {
        private String email;
        private String motDePasse;

        public String getEmail() {
            return email;
        }

        public String getMotDePasse() {
            return motDePasse;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setMotDePasse(String motDePasse) {
            this.motDePasse = motDePasse;
        }
    }
}
