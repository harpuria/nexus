package com.qwerty.nexus.domain.management.game.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GameCreateRequestDto {
    @Schema(example = "1")
    private int orgId;

    @Schema(example = "그리즐리키우기")
    private String name;

    @Schema(example = "/images/games/grizzly-raising.png")
    private String imagePath;

    @Schema(example = "sample-client-id.apps.googleusercontent.com")
    private String googleClientId;

    @Schema(example = "sample-google-client-secret")
    private String googleClientSecret;

    @Schema(example = "admin")
    private String createdBy;

    @Schema(example = "1.0.0")
    private String version;
}
