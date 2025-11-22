package com.example.Farcal_Back.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/success")
    public Mono<Map<String, Object>> success(Authentication authentication) {
        var principal = (DefaultOidcUser) authentication.getPrincipal();

        return Mono.just(Map.of(
                "id", principal.getSubject(),
                "email", principal.getEmail(),
                "name", principal.getFullName(),
                "picture", principal.getPicture()
        ));
    }
}

