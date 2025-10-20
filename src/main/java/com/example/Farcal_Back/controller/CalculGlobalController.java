package com.example.Farcal_Back.controller;

import com.example.Farcal_Back.model.CalculGlobal;
import com.example.Farcal_Back.repository.CalculGlobalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/calculs-globaux")
@CrossOrigin(origins = "*")
public class CalculGlobalController {

    private final CalculGlobalRepository repo;

    public CalculGlobalController(CalculGlobalRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public Flux<CalculGlobal> getAll() {
        return repo.findAll();
    }

    @GetMapping("/utilisateur/{id}")
    public Flux<CalculGlobal> getByUtilisateur(@PathVariable UUID id) {
        return repo.findByUtilisateurId(id);
    }

    @GetMapping("/date/{date}")
    public Flux<CalculGlobal> getByDate(@PathVariable String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        return repo.findByDateCalcul(parsedDate);
    }

    @PostMapping
    public Mono<ResponseEntity<String>> save(@RequestBody CalculGlobal calcul) {
        return repo.save(calcul)
                .map(c -> ResponseEntity.ok("Calcul global enregistré"))
                .onErrorResume(e ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Erreur : " + e.getMessage())));
    }
}
