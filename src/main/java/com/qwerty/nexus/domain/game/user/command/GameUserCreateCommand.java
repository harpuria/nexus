package com.qwerty.nexus.domain.game.user.command;

import com.qwerty.nexus.domain.game.user.dto.request.GameUserCreateRequestDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameUserCreateCommand {
    private Integer gameId;
    private String userLId;
    private String userLPw;
    private String nickname;
    private String loginType;
    private String device;
    private String createdBy;

    public static GameUserCreateCommand from(GameUserCreateRequestDto dto){
        return GameUserCreateCommand.builder()
                .gameId(dto.getGameId())
                .userLId(dto.getUserLId())
                .userLPw(dto.getUserLPw())
                .nickname(dto.getNickname())
                .loginType(dto.getLoginType())
                .device(dto.getDevice())
                .createdBy(dto.getCreatedBy())
                .build();
    }
}
