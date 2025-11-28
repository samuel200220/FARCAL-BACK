package com.example.Farcal_Back.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
public class EmailVerificationService {

    private final JavaMailSender mailSender;
    private final SecureRandom random = new SecureRandom();

    public EmailVerificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public String generateVerificationCode() {
        int code = random.nextInt(900000) + 100000; // Génère un code entre 100000 et 999999
        return String.valueOf(code);
    }

    public LocalDateTime calculateExpirationTime() {
        return LocalDateTime.now().plusMinutes(10); // Code valide 10 minutes
    }

    public boolean isCodeExpired(LocalDateTime expiration) {
        return LocalDateTime.now().isAfter(expiration);
    }

    public Mono<Void> sendVerificationEmail(String email, String code) {
        return Mono.fromFuture(CompletableFuture.runAsync(() -> {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(email);
                message.setSubject("Vérification de votre email - Farcal");
                message.setText(
                        "Bonjour,\n\n" +
                                "Votre code de vérification est : " + code + "\n\n" +
                                "Ce code expirera dans 10 minutes.\n\n" +
                                "Si vous n'avez pas créé de compte, veuillez ignorer cet email.\n\n" +
                                "Cordialement,\nL'équipe Farcal"
                );
                message.setFrom("noreply@farcal.com");

                mailSender.send(message);
                System.out.println("Email de vérification envoyé à: " + email + " - Code: " + code);
            } catch (Exception e) {
                System.err.println("Erreur lors de l'envoi de l'email: " + e.getMessage());
                throw new RuntimeException("Erreur lors de l'envoi de l'email", e);
            }
        }));
    }
}