package com.qwerty.nexus.domain.game.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.qwerty.nexus.domain.game.auth.Provider;
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
    @Schema(example = "eyJhbGciOiJIUzI1NiJ9.sample")
    private String idToken;
    @Schema(example = "1")
    private String clientId;
    @Schema(example = "sample")
    private String clientSecret;

    // APPLE

    // META

    // KAKAO

    // NAVER

    @JsonIgnore
    @Schema(hidden = true)
    private GoogleIdToken googleIdToken;
}
