package com.example.Farcal_Back.controller;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
public class ProductionDebugController {

    private final JavaMailSender mailSender;

    public ProductionDebugController(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @GetMapping("/production-test")
    public Mono<Map<String, Object>> productionTest() {
        return Mono.fromCallable(() -> {
            Map<String, Object> result = new HashMap<>();

            try {
                // Test configuration email
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo("samuelftagat@gmail.com");
                message.setSubject("Test Production Render - " + LocalDateTime.now());
                message.setText("Test réussi depuis Render!");
                message.setFrom("samuelftagat@gmail.com");

                mailSender.send(message);
                result.put("email_status", "SUCCESS");
                result.put("email_message", "Email envoyé avec succès");
            } catch (Exception e) {
                result.put("email_status", "FAILED");
                result.put("email_error", e.getMessage());
                result.put("email_error_type", e.getClass().getName());
            }

            // Informations système
            result.put("environment", "PRODUCTION");
            result.put("timestamp", LocalDateTime.now().toString());
            result.put("gmail_username_set", System.getenv("GMAIL_USERNAME") != null);
            result.put("gmail_password_set", System.getenv("GMAIL_APP_PASSWORD") != null);

            return result;
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
