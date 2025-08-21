package com.qwerty.nexus.domain.management.game.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameUpdateCommand {
    private int gameId;
    private String name;
    private String status;
    private String isDel;
    private String updatedBy;
}
