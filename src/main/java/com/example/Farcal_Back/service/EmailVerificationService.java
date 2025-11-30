package com.example.Farcal_Back.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import jakarta.mail.internet.MimeMessage;
import jakarta.mail.MessagingException;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class EmailVerificationService {

    private final JavaMailSender mailSender;
    private final SecureRandom random = new SecureRandom();

    public EmailVerificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public String generateVerificationCode() {
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }

    public LocalDateTime calculateExpirationTime() {
        return LocalDateTime.now().plusMinutes(10);
    }

    public boolean isCodeExpired(LocalDateTime expiration) {
        return LocalDateTime.now().isAfter(expiration);
    }

    public Mono<Boolean> sendVerificationEmail(String toEmail, String code) {
        return Mono.fromCallable(() -> {
            try {
                return sendHtmlEmail(toEmail, code);
            } catch (Exception e) {
                System.err.println("❌ Erreur email HTML: " + e.getMessage());
                // Fallback vers email simple
                return sendSimpleEmail(toEmail, code);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private boolean sendHtmlEmail(String toEmail, String code) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8"); // false = pas multipart

            helper.setTo(toEmail);
            helper.setSubject("🔐 Vérification de votre email - Farcal");
            helper.setFrom("samuelftagat@gmail.com", "Farcal_Prod");

            String htmlContent = buildHtmlEmailContent(code);
            helper.setText(htmlContent, true); // true = HTML

            mailSender.send(mimeMessage);
            System.out.println("✅ Email HTML envoyé avec succès à : " + toEmail);
            return true;
        } catch (MessagingException e) {
            System.err.println("❌ Erreur MIME: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.err.println("❌ Erreur générale: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private boolean sendSimpleEmail(String toEmail, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Code de vérification Farcal");
            message.setText(
                    "Votre code de vérification Farcal est : " + code + "\n\n" +
                            "Ce code expire dans 10 minutes.\n\n" +
                            "Si vous n'avez pas créé de compte, ignorez cet email.\n\n" +
                            "Cordialement,\nL'équipe Farcal"
            );
            message.setFrom("noreply@farcal.com");

            mailSender.send(message);
            System.out.println("✅ Email simple envoyé à : " + toEmail);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Échec email simple: " + e.getMessage());
            return false;
        }
    }

    private String buildHtmlEmailContent(String code) {
        // Version simplifiée du HTML pour éviter les problèmes d'encodage
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background: #f5f5f5; }
                    .container { max-width: 600px; margin: 0 auto; background: white; border-radius: 10px; padding: 30px; }
                    .header { background: #1D4ED8; color: white; padding: 20px; text-align: center; border-radius: 8px; margin-bottom: 20px; }
                    .code { font-size: 32px; font-weight: bold; color: #1D4ED8; text-align: center; margin: 20px 0; }
                    .footer { margin-top: 20px; padding-top: 20px; border-top: 1px solid #ddd; text-align: center; color: #666; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Farcal</h1>
                        <p>Votre calculateur de tarifs intelligent</p>
                    </div>
                    
                    <h2>Vérification de votre email</h2>
                    <p>Bonjour,</p>
                    <p>Merci de vous être inscrit sur Farcal ! Voici votre code de vérification :</p>
                    
                    <div class="code">%s</div>
                    
                    <p><strong>⚠️ Ce code expirera dans 10 minutes.</strong></p>
                    
                    <p>Si vous n'avez pas créé de compte Farcal, veuillez ignorer cet email.</p>
                    
                    <div class="footer">
                        <p>Cordialement,<br><strong>L'équipe Farcal</strong></p>
                        <p><a href="https://fare-calculator-web-app-pcto.vercel.app">https://fare-calculator-web-app-pcto.vercel.app</a></p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(code);
    }
}