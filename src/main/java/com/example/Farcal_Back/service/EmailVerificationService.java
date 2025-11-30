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
                    System.out.println("🚀 Début envoi email à: " + toEmail);

                    // Essayer d'abord l'email simple (plus fiable sur Render)
                    boolean simpleSuccess = sendSimpleEmail(toEmail, code);
                    if (simpleSuccess) {
                        return true;
                    }

                    // Si simple échoue, essayer HTML
                    System.out.println("🔄 Essai avec email HTML...");
                    return sendHtmlEmail(toEmail, code);

                }).subscribeOn(Schedulers.boundedElastic())
                .timeout(java.time.Duration.ofSeconds(15))
                .onErrorReturn(false);
    }

    private boolean sendSimpleEmail(String toEmail, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Farcal - Code de vérification: " + code);
            message.setText(
                    "─────────────────────────────────────\n" +
                            "           🚗 FARCAL\n" +
                            "─────────────────────────────────────\n\n" +
                            "Votre code de vérification : " + code + "\n\n" +
                            "⏰ Valable 10 minutes\n\n" +
                            "Merci pour votre inscription !\n\n" +
                            "Cordialement,\n" +
                            "L'équipe Farcal\n\n" +
                            "🌐 https://fare-calculator-web-app-pcto.vercel.app\n" +
                            "─────────────────────────────────────"
            );
            // Utiliser l'email réel de Gmail comme expéditeur
            message.setFrom("samuelftagat@gmail.com");

            mailSender.send(message);
            System.out.println("✅ Email simple envoyé avec succès à: " + toEmail);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Échec email simple: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            return false;
        }
    }

    private boolean sendHtmlEmail(String toEmail, String code) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Vérification Farcal - Code: " + code);
            helper.setFrom("samuelftagat@gmail.com", "Équipe Farcal");

            String htmlContent = buildSimpleHtmlEmail(code);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            System.out.println("✅ Email HTML envoyé avec succès à: " + toEmail);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Échec email HTML: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            return false;
        }
    }

    private String buildSimpleHtmlEmail(String code) {
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