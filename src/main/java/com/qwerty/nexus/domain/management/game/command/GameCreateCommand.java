package com.qwerty.nexus.domain.management.game.command;

import lombok.Builder;
import lombok.Getter;

/**
 * 게임 정보 생성용 Command
 */
@Getter
@Builder
public class GameCreateCommand {
    private int orgId;
    private String name;
    private String createBy;
}
