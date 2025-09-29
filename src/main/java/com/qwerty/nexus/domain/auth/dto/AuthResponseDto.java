package com.qwerty.nexus.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponseDto {
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;         // Access Token 만료 시간 (초)
    private Long refreshExpiresIn;  // Refresh Token 만료 시간 (초)
}

