package com.qwerty.nexus.domain.gameUser.command;

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
}
