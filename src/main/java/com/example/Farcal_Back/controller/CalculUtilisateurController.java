package com.example.Farcal_Back.controller;

import com.example.Farcal_Back.model.CalculUtilisateur;
import com.example.Farcal_Back.repository.CalculUtilisateurRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/calculs-utilisateur")
@CrossOrigin(origins = "*")
public class CalculUtilisateurController {

    private final CalculUtilisateurRepository repo;

    public CalculUtilisateurController(CalculUtilisateurRepository repo) {
        this.repo = repo;
    }

    // 🔹 Récupérer tous les calculs d’un utilisateur
    @GetMapping("/utilisateur/{id}")
    public Flux<CalculUtilisateur> getByUtilisateur(@PathVariable UUID id) {
        return repo.findByIdUtilisateur(id);
    }

    // 🔹 Sauvegarder un nouveau calcul
    @PostMapping
    public Mono<ResponseEntity<String>> save(@RequestBody CalculUtilisateur calcul) {

        // ❌ NE PAS définir l'id ici
        calcul.setTimestamp(Instant.now());

        return repo.save(calcul)
                .map(c -> ResponseEntity.ok("Calcul enregistré avec succès ✅"))
                .onErrorResume(e -> Mono.just(
                        ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Erreur : " + e.getMessage())));
    }

    // 🔴 Supprimer un calcul par ID
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable UUID id) {
        return repo.existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                    return repo.deleteById(id)
                            .then(Mono.just(ResponseEntity.noContent().build()));
                });
    }
}
