package com.qwerty.nexus.domain.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.qwerty.nexus.domain.auth.Provider;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthRequestDto {
    @Schema(example = "1")
    private int gameId;

    @Schema(example = "GOOGLE")
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
