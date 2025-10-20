package com.example.Farcal_Back.repository;

import com.example.Farcal_Back.model.CalculGlobal;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.UUID;

public interface CalculGlobalRepository extends ReactiveCrudRepository<CalculGlobal, Long> {

    // 🔍 Récupérer tous les calculs d’un utilisateur
    Flux<CalculGlobal> findByUtilisateurId(UUID utilisateurId);

    // 🔍 Récupérer tous les calculs d’une date donnée
    Flux<CalculGlobal> findByDateCalcul(LocalDate dateCalcul);
}
