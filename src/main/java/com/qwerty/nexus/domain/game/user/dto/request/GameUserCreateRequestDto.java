package com.qwerty.nexus.domain.game.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import com.qwerty.nexus.domain.game.auth.Provider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GameUserCreateRequestDto {
    @Schema(example = "1")
    private Integer gameId;

    @Schema(example = "1")
    private String userLId;

    @Schema(example = "P@ssw0rd!")
    private String userLPw;

    @Schema(example = "테스트유저")
    private String nickname;

    @Schema(example = "1")
    private String socialId;

    @Schema(example = "GOOGLE")
    private Provider provider;

    @Schema(example = "ANDROID")
    private String device;

    @Schema(example = "admin")
    private String createdBy;
}
