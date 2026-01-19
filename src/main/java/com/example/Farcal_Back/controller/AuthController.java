package com.example.Farcal_Back.controller;

import com.example.Farcal_Back.repository.UtilisateurSimpleRepository;
import com.example.Farcal_Back.model.UtilisateurSimple;
import com.example.Farcal_Back.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.util.UUID;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UtilisateurSimpleRepository repository;
    private final JwtService jwtService;

    public AuthController(UtilisateurSimpleRepository repository, JwtService jwtService) {
        this.repository = repository;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, Object>>> login(
            @RequestBody Map<String, String> request
    ) {
        String email = request.get("email");

        return repository.findByEmail(email)
                .map(user -> {
                    String token = jwtService.generateToken(
                            user.getId(),
                            user.getEmail()
                    );

                    return ResponseEntity.ok(
                            Map.of(
                                    "token", token,
                                    "user", user
                            )
                    );
                })
                .defaultIfEmpty(
                        ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(Map.of("error", "Utilisateur introuvable"))
                );
    }

    @GetMapping("/me")
    public Mono<ResponseEntity<UtilisateurSimple>> me(
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return repository.findById(userId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}


