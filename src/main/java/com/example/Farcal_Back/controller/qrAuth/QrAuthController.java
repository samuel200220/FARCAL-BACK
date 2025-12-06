package com.example.Farcal_Back.controller.qrAuth;

import com.example.Farcal_Back.DTO.qrAuth.QrApproveRequest;
import com.example.Farcal_Back.service.qrAuth.HmacService;
import com.example.Farcal_Back.service.qrAuth.QrAuthService;
import com.example.Farcal_Back.model.qrAuth.QrAuthToken;
import com.example.Farcal_Back.DTO.qrAuth.QrCreateResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth/qr")
public class QrAuthController {

    private final QrAuthService qrAuthService;
    private final HmacService hmacService;

    public QrAuthController(QrAuthService qrAuthService, HmacService hmacService) {
        this.qrAuthService = qrAuthService;
        this.hmacService = hmacService;
    }

    //Demande de l'app web pour creer le token securise
    @PostMapping("/create")
    public ResponseEntity<QrCreateResponse> createQr() {

        // Ex : identifiant unique de session
        String sessionId = UUID.randomUUID().toString();

        String tokenId = qrAuthService.createToken(sessionId);

        // Créer un cookie HttpOnly
        ResponseCookie cookie = ResponseCookie.from("qr_session", sessionId)
                .httpOnly(true)
                .secure(true) // obligatoire en HTTPS
                .sameSite("Strict")
                .path("/")
                .maxAge(300) // 3 min
                .build();

        // Associer token → session
        qrAuthService.linkSession(tokenId, sessionId);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new QrCreateResponse(tokenId));
    }

    // Utiliser par le mobile pour renvoyer le token et l'id de l'utilisateur
    @PostMapping("/approve")
    public ResponseEntity<Map<String, Object>> approveQr(@RequestBody QrApproveRequest request) {
        QrAuthToken token = qrAuthService.getToken(request.getTokenId());

        // Vérifier HMAC pour s'assurer que token + session n'ont pas été modifiés
        if (!hmacService.verify(token.getId(), token.getSessionId(), token.getHmac())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("success", false, "error", "HMAC invalid"));
        }

        //Verifie si le token a deja ete valide
        if (token.isValidated()) {
            return ResponseEntity.status(HttpStatus.CONFLICT) // 409
                    .body(Map.of("success", false, "error", "Token already validated"));
        }


        // Valider le token avec le userId provenant du mobile
        qrAuthService.validateToken(request.getTokenId(), request.getUserId());

        return ResponseEntity.ok(Map.of("success", true)); // <-- 200 OK si tout est bon

    }

    // Demande de l'app web pour savoir si le token est valide
    @GetMapping("/check/{tokenId}")
    public ResponseEntity<Map<String, Object>> check(@PathVariable String tokenId,
                                                     @CookieValue("qr_session") String sessionId) {

        QrAuthToken token;

        try {
            token = qrAuthService.getToken(tokenId);
        } catch (RuntimeException e) {
            // Token expiré OU introuvable
            return ResponseEntity.status(HttpStatus.GONE)   // 410 Gone
                    .body(Map.of(
                            "validated", false,
                            "error", "TOKEN_EXPIRED"
                    ));
        }

        if (!hmacService.verify(token.getId(), token.getSessionId(), token.getHmac())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("validated", false, "error", "HMAC invalid"));
        }

        //Pour verifier que la session effectuant le check est la meme qui a demande la creation du token
        if (!token.getSessionId().equals(sessionId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("validated", false, "error", "Session invalid"));
        }

        Map<String, Object> body = new HashMap<>();
        body.put("validated", token.isValidated());
        body.put("userId", token.getUserId()); // OK même si null

        return ResponseEntity.ok(body);
    }

}