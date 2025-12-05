package com.example.Farcal_Back.DTO.qrAuth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QrApproveRequest {
    private String tokenId;
    private Long userId;
}
