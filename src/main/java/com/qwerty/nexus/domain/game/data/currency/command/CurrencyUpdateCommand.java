package com.qwerty.nexus.domain.game.data.currency.command;

import com.qwerty.nexus.domain.game.data.currency.dto.request.CurrencyUpdateRequestDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CurrencyUpdateCommand {
    private Integer currencyId;
    private String name;
    private String desc;
    private Long maxAmount;
    private String updatedBy;
    private String isDel;

    public static CurrencyUpdateCommand from(CurrencyUpdateRequestDto dto){
        return CurrencyUpdateCommand.builder()
                .currencyId(dto.getCurrencyId())
                .name(dto.getName())
                .desc(dto.getDesc())
                .maxAmount(dto.getMaxAmount())
                .updatedBy(dto.getUpdatedBy())
                .isDel(dto.getIsDel())
                .build();
    }
}
