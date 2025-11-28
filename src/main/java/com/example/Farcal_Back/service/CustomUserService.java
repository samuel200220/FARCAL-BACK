package com.example.Farcal_Back.service;

import com.example.Farcal_Back.model.UtilisateurSimple;
import com.example.Farcal_Back.repository.UtilisateurSimpleRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;

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
                                null,
                                nom,
                                email,
                                Instant.now()
                        )).doOnSuccess(user -> {
                            // Pour les utilisateurs OAuth2, marquer l'email comme vérifié directement
                            user.setEmailVerifie(true);
                            user.setProvider("google");
                            repo.save(user).subscribe();
                        })
                )
                .flatMap(user -> {
                    // Si l'utilisateur existe mais n'est pas marqué comme OAuth2, le mettre à jour
                    if (!"google".equals(user.getProvider())) {
                        user.setProvider("google");
                        user.setEmailVerifie(true); // Les emails OAuth2 sont considérés comme vérifiés
                        return repo.save(user);
                    }
                    return Mono.just(user);
                });
    }
}