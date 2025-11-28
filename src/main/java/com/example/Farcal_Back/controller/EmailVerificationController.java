package com.example.Farcal_Back.controller;

import com.example.Farcal_Back.model.UtilisateurSimple;
import com.example.Farcal_Back.repository.UtilisateurSimpleRepository;
import com.example.Farcal_Back.service.EmailVerificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class EmailVerificationController {

    private final UtilisateurSimpleRepository userRepository;
    private final EmailVerificationService verificationService;

    public EmailVerificationController(UtilisateurSimpleRepository userRepository,
                                       EmailVerificationService verificationService) {
        this.userRepository = userRepository;
        this.verificationService = verificationService;
    }

    @PostMapping("/send-verification")
    public Mono<ResponseEntity<Map<String, Object>>> sendVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        return userRepository.findByEmail(email)
                .flatMap(user -> {
                    // Générer un nouveau code
                    String code = verificationService.generateVerificationCode();
                    user.setCodeVerification(code);
                    user.setExpirationCode(verificationService.calculateExpirationTime());

                    return userRepository.save(user)
                            .then(verificationService.sendVerificationEmail(email, code))
                            .then(Mono.just(ResponseEntity.ok(createResponse(true, "Code de vérification envoyé avec succès"))));
                })
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createResponse(false, "Utilisateur non trouvé"))))
                .onErrorResume(error -> {
                    System.err.println("Erreur lors de l'envoi du code: " + error.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(createResponse(false, "Erreur lors de l'envoi du code")));
                });
    }

    @PostMapping("/verify-email")
    public Mono<ResponseEntity<Map<String, Object>>> verifyEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");

        return userRepository.findByEmail(email)
                .flatMap(user -> {
                    // Vérifier si l'email est déjà vérifié
                    if (Boolean.TRUE.equals(user.getEmailVerifie())) {
                        Map<String, Object> response = createResponse(true, "Email déjà vérifié");
                        response.put("user", createUserResponse(user));
                        return Mono.just(ResponseEntity.ok(response));
                    }

                    // Vérifier si le code a expiré
                    if (verificationService.isCodeExpired(user.getExpirationCode())) {
                        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(createResponse(false, "Code expiré")));
                    }

                    // Vérifier si le code correspond
                    if (!code.equals(user.getCodeVerification())) {
                        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(createResponse(false, "Code invalide")));
                    }

                    // Marquer l'email comme vérifié et nettoyer le code
                    user.setEmailVerifie(true);
                    user.setCodeVerification(null);
                    user.setExpirationCode(null);

                    return userRepository.save(user)
                            .map(savedUser -> {
                                Map<String, Object> response = createResponse(true, "Email vérifié avec succès");
                                response.put("user", createUserResponse(savedUser));
                                return ResponseEntity.ok(response);
                            });
                })
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createResponse(false, "Utilisateur non trouvé"))))
                .onErrorResume(error -> {
                    System.err.println("Erreur lors de la vérification: " + error.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(createResponse(false, "Erreur lors de la vérification")));
                });
    }

    @GetMapping("/check-email/{email}")
    public Mono<ResponseEntity<Map<String, Object>>> checkEmailExists(@PathVariable String email) {
        return userRepository.existsByEmail(email)
                .map(exists -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("exists", exists);
                    return ResponseEntity.ok(response);
                })
                .defaultIfEmpty(ResponseEntity.ok(createResponse(false, "Erreur de vérification")));
    }

    // Méthodes utilitaires pour créer des réponses cohérentes
    private Map<String, Object> createResponse(boolean success, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", message);
        return response;
    }

    private Map<String, Object> createUserResponse(UtilisateurSimple user) {
        Map<String, Object> userResponse = new HashMap<>();
        userResponse.put("id", user.getId());
        userResponse.put("email", user.getEmail());
        userResponse.put("nom", user.getNom());
        userResponse.put("emailVerifie", user.getEmailVerifie());
        return userResponse;
    }
}