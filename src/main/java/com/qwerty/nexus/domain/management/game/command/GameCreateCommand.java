package com.qwerty.nexus.domain.management.game.command;

import com.qwerty.nexus.domain.management.game.dto.request.GameCreateRequestDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameCreateCommand {
    private int orgId;
    private String name;
    private String version;
    private String createdBy;

    public static GameCreateCommand from(GameCreateRequestDto dto){
        return GameCreateCommand.builder()
                .orgId(dto.getOrgId())
                .name(dto.getName())
                .createdBy(dto.getCreatedBy())
                .version(dto.getVersion())
                .build();
    }
}
