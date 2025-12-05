package com.example.Farcal_Back.model.qrAuth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class QrAuthToken implements Serializable {

    private String id;
    private String sessionId;  // UUID
    private UUID userId; // rempli après la validation mobile
    private boolean validated = false;
    private LocalDateTime createdAt;
    private String hmac;  // ← IMPORTANT pour HMAC


}