package com.qwerty.nexus.domain.game.command;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class GameUpdateCommand {
    private String name;
    private String status;
    private String isDel;
    private String updateBy;
}
