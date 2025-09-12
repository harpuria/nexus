package com.qwerty.nexus.domain.game.data.currency.command;

import com.qwerty.nexus.domain.game.data.currency.dto.request.CurrencyCreateRequestDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CurrencyCreateCommand {
    private Integer gameId;
    private String name;
    private String desc;
    private Long maxAmount;
    private String createdBy;

    public static CurrencyCreateCommand from(CurrencyCreateRequestDto dto){
        return CurrencyCreateCommand.builder()
                .gameId(dto.getGameId())
                .name(dto.getName())
                .desc(dto.getDesc())
                .maxAmount(dto.getMaxAmount())
                .createdBy(dto.getCreatedBy())
                .build();
    }
}
