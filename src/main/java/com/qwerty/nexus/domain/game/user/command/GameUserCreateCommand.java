package com.qwerty.nexus.domain.game.user.command;

import com.qwerty.nexus.domain.auth.Provider;
import com.qwerty.nexus.domain.game.user.dto.request.GameUserCreateRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameUserCreateCommand {
    private Integer gameId;
    private String userLId;
    private String userLPw;
    private String nickname;
    private Provider provider;
    private String socialId;
    private String device;
    private String createdBy;

    public static GameUserCreateCommand from(GameUserCreateRequestDto dto){
        return GameUserCreateCommand.builder()
                .gameId(dto.getGameId())
                .userLId(dto.getUserLId())
                .userLPw(dto.getUserLPw())
                .nickname(dto.getNickname())
                .provider(dto.getProvider())
                .socialId(dto.getSocialId())
                .device(dto.getDevice())
                .createdBy(dto.getCreatedBy())
                .build();
    }
}
