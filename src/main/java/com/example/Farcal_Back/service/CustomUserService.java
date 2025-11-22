package com.example.Farcal_Back.service;

import com.example.Farcal_Back.model.UtilisateurSimple;
import com.example.Farcal_Back.repository.UtilisateurSimpleRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Service
public class CustomUserService {

    private final UtilisateurSimpleRepository repo;

    public CustomUserService(UtilisateurSimpleRepository repo) {
        this.repo = repo;
    }

    public Mono<UtilisateurSimple> createOrGetUser(String nom, String email) {
        return repo.findByEmail(email)
                .switchIfEmpty(
                        repo.save(new UtilisateurSimple(
                                null,              // 👈 IMPORTANT : laisser null
                                nom,
                                email,
                                Instant.now()
                        ))
                );
    }

}
