package com.qwerty.nexus.domain.game.user.dto.response;

import com.qwerty.nexus.domain.game.user.entity.GameUserEntity;
import com.qwerty.nexus.global.extend.dto.BaseResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
public class GameUserLoginResponseDto {
    private String accessToken;
    private String tokenType = "Bearer";
    private Long expiresIn;
}
