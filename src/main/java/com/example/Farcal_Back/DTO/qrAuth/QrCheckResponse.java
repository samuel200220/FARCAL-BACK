package com.example.Farcal_Back.DTO.qrAuth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class QrCheckResponse {
    private boolean validated;
    private Long userId; // si validé, sinon null
}

