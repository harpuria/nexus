package com.qwerty.nexus.domain.game.command;

import lombok.Builder;
import lombok.Getter;

/**
 * 게임 정보 생성용 Command
 */
@Getter
@Builder
public class GameCreateCommand {
    private String name;
    private String createBy;
}
