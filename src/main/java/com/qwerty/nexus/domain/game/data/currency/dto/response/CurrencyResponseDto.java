package com.qwerty.nexus.domain.game.data.currency.dto.response;

import com.qwerty.nexus.domain.game.data.currency.entity.CurrencyEntity;
import com.qwerty.nexus.global.paging.dto.BaseResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class CurrencyResponseDto extends BaseResponseDto {
    private Integer currencyId;
    private Integer gameId;
    private String name;
    private String desc;
    private Long maxAmount;

    public static CurrencyResponseDto from(CurrencyEntity entity){
        return CurrencyResponseDto.builder()
                .currencyId(entity.getCurrencyId())
                .gameId(entity.getGameId())
                .name(entity.getName())
                .desc(entity.getDesc())
                .maxAmount(entity.getMaxAmount())
                .build();
    }
}
