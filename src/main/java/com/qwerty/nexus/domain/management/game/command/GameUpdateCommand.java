package com.qwerty.nexus.domain.management.game.command;

import com.qwerty.nexus.domain.management.game.dto.request.GameUpdateRequestDto;
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

    public static GameUpdateCommand from(GameUpdateRequestDto dto){
        return GameUpdateCommand.builder()
                .gameId(dto.getGameId())
                .name(dto.getName())
                .status(dto.getStatus())
                .isDel(dto.getIsDel())
                .updatedBy(dto.getUpdateBy())
                .build();
    }
}
