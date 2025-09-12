package com.qwerty.nexus.domain.game.user.command;

import com.qwerty.nexus.domain.game.user.dto.request.GameUserBlockRequestDto;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
public class GameUserBlockCommand {
    private Integer userId;
    private OffsetDateTime blockStartDate;
    private OffsetDateTime blockEndDate;
    private String blockReason;
    private int blockDay;
    private String updatedBy;

    public static GameUserBlockCommand from(GameUserBlockRequestDto dto){
        return GameUserBlockCommand.builder()
                .userId(dto.getUserId())
                .blockStartDate(dto.getBlockStartDate())
                .blockEndDate(dto.getBlockEndDate())
                .blockReason(dto.getBlockReason())
                .blockDay(dto.getBlockDay())
                .updatedBy(dto.getUpdatedBy())
                .build();
    }
}
