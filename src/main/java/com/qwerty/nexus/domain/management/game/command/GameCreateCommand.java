package com.qwerty.nexus.domain.management.game.command;

import com.qwerty.nexus.domain.management.game.dto.request.GameCreateRequestDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameCreateCommand {
    private int orgId;
    private String name;
    private String createBy;

    public static GameCreateCommand from(GameCreateRequestDto dto){
        return GameCreateCommand.builder()
                .orgId(dto.getOrgId())
                .name(dto.getName())
                .createBy(dto.getCreateBy())
                .build();
    }
}
