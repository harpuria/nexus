package com.qwerty.nexus.global.util;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtTokenGenerationData {
    String socialProvider;
    String socialId;
    String email;
    String emailVerified;
    String accessToken;
    String refreshToken;
}
