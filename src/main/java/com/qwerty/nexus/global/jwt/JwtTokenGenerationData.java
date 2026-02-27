package com.qwerty.nexus.global.jwt;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtTokenGenerationData {
    String provider;
    String socialId;
    String email;
    String emailVerified;
    String accessToken;
    String refreshToken;
}
