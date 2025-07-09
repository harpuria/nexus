package com.qwerty.nexus.domain.game.command;

import lombok.Builder;
import lombok.Getter;

/**
 * 게임 정보 수정용 Command
 */
@Getter
@Builder
public class GameUpdateCommand {
    private String name;
    private String status;
    private String isDel;
    private String updateBy;
}
