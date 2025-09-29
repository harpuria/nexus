package com.qwerty.nexus.domain.auth.commnad;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.qwerty.nexus.domain.auth.Provider;
import com.qwerty.nexus.domain.auth.dto.AuthRequestDto;
import lombok.Builder;
import lombok.Getter;

import java.security.GeneralSecurityException;

@Getter
@Builder
public class AuthCommand {
    private Provider provider;

    // GOOGLE
    private String idToken;
    private String clientId;
    private String clientSecret;

    // APPLE

    // META

    // KAKAO

    // NAVER

    private GoogleIdToken googleIdToken;

    public static AuthCommand from(AuthRequestDto dto) {
        return AuthCommand.builder()
                .provider(dto.getProvider())
                .idToken(dto.getIdToken())
                .clientId(dto.getClientId())
                .clientSecret(dto.getClientSecret())
                .googleIdToken(dto.getGoogleIdToken())
                .build();
    }
}
