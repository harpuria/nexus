package com.qwerty.nexus.domain.game.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameCreateCommand {
    private String name;
    private String createBy;
}
