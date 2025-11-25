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
                        .pathMatchers("/api/**").permitAll()
                        .pathMatchers("/auth/**").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2Login(oauth -> oauth.authenticationSuccessHandler((webFilterExchange, authentication) -> {

                    var oauthUser = (org.springframework.security.oauth2.core.user.OAuth2User)
                            authentication.getPrincipal();

                    String nom = oauthUser.getAttribute("name");
                    String email = oauthUser.getAttribute("email");

                    // 👉 Enregistrer automatiquement l’utilisateur
                    return customUserService.createOrGetUser(nom, email)
                            .then(
                                    Mono.fromRunnable(() -> {
                                        var exchange = webFilterExchange.getExchange();
                                        exchange.getResponse().setStatusCode(HttpStatus.FOUND);
                                        exchange.getResponse().getHeaders().setLocation(
                                                URI.create("https://fare-calculator-web-app-pcto.vercel.app/accueil")
//                                                URI.create("http://localhost:3000/accueil")
                                        );
                                    })
                            ).then();
                }))
                .build();
    }
}
