package com.example.Farcal_Back.config;

import com.example.Farcal_Back.service.CustomUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

import java.net.URI;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final CustomUserService customUserService;

    public SecurityConfig(CustomUserService customUserService) {
        this.customUserService = customUserService;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                "/api/auth/login",
                                "/api/utilisateurs/register",
                                "/api/utilisateurs/health",
                                "/api/entreprises/**",
                                "/api/calculs-utilisateur/**",
                                "/api/contributions/**",
                                "/auth/**"
                        ).permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth -> oauth.jwt()) // JWT OK
                .oauth2Login(oauth -> oauth.authenticationSuccessHandler((webFilterExchange, authentication) -> {

                    var oauthUser = (org.springframework.security.oauth2.core.user.OAuth2User)
                            authentication.getPrincipal();

                    String nom = oauthUser.getAttribute("name");
                    String email = oauthUser.getAttribute("email");

                    return customUserService.createOrGetUser(nom, email)
                            .then(Mono.fromRunnable(() -> {
                                var exchange = webFilterExchange.getExchange();
                                exchange.getResponse().setStatusCode(HttpStatus.FOUND);
                                exchange.getResponse().getHeaders().setLocation(
                                        URI.create("https://fare-calculator-web-app-pcto.vercel.app/accueil")
                                );
                            })).then();
                }))
                .build();
    }

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        String secret = "LrLeZKyerbH43sw1/qdlZMIRgpj5EC50LBNSO3FhSGw=";
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        SecretKey key = new SecretKeySpec(decodedKey, "HmacSHA256");

        return NimbusReactiveJwtDecoder.withSecretKey(key).build();
    }
}

