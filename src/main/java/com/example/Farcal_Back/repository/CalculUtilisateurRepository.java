package com.example.Farcal_Back.repository;

import com.example.Farcal_Back.model.CalculUtilisateur;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import java.util.UUID;

public interface CalculUtilisateurRepository extends ReactiveCrudRepository<CalculUtilisateur, UUID> {

    // 🔍 Récupérer tous les calculs d’un utilisateur
    @Query("SELECT * FROM calculs_utilisateur WHERE id_utilisateur = :idUtilisateur ORDER BY timestamp DESC")
    Flux<CalculUtilisateur> findByIdUtilisateur(UUID idUtilisateur);
}
