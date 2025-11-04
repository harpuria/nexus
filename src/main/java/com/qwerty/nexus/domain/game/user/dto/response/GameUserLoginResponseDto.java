package com.qwerty.nexus.domain.game.user.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameUserLoginResponseDto {
    private String accessToken;
    private String tokenType = "Bearer";
    private Long expiresIn;
}
