package com.example.Farcal_Back.repository;

import com.example.Farcal_Back.model.Entreprise;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface EntrepriseRepository extends ReactiveCrudRepository<Entreprise, UUID> {

    @Query("SELECT * FROM entreprise WHERE email = :email LIMIT 1")
    Mono<Entreprise> findByEmail(String email);
}
