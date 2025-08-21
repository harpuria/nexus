package com.qwerty.nexus.domain.management.game.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameCreateCommand {
    private int orgId;
    private String name;
    private String createBy;
}
