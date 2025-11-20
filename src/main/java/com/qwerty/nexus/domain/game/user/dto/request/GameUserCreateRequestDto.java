package com.qwerty.nexus.domain.game.user.dto.request;

import com.qwerty.nexus.domain.auth.Provider;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GameUserCreateRequestDto {
    @Schema(example = "1")
    private Integer gameId;

    @Schema(example = "userTest")
    private String userLId;

    @Schema(example = "userTest")
    private String userLPw;

    @Schema(example = "테스트유저")
    private String nickname;

    @Schema(example ="socialIdTest")
    private String socialId;

    @Schema(example = "GOOGLE")
    private Provider provider;

    @Schema(example = "SAMSUNG")
    private String device;

    @Schema(example = "userTest")
    private String createdBy;
}
