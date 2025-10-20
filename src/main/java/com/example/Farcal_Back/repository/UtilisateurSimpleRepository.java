package com.example.Farcal_Back.repository;

import com.example.Farcal_Back.model.UtilisateurSimple;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UtilisateurSimpleRepository extends ReactiveCrudRepository<UtilisateurSimple, UUID> {

    @Query("SELECT * FROM utilisateur_simple WHERE email = :email LIMIT 1")
    Mono<UtilisateurSimple> findByEmail(@Param("email") String email);
}
