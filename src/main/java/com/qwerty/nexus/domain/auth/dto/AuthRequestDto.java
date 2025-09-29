package com.qwerty.nexus.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.qwerty.nexus.domain.auth.Provider;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequestDto {
    private Provider provider;

    // GOOGLE
    private String idToken;
    private String clientId;
    private String clientSecret;

    // APPLE

    // META

    // KAKAO

    // NAVER

    @JsonIgnore
    private GoogleIdToken googleIdToken;
}
