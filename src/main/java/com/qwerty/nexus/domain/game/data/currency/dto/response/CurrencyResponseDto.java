package com.qwerty.nexus.domain.game.data.currency.dto.response;

import com.qwerty.nexus.domain.game.data.currency.entity.CurrencyEntity;
import com.qwerty.nexus.global.extend.dto.BaseResponseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyResponseDto extends BaseResponseDto {
    private Integer currencyId;
    private Integer gameId;
    private String name;
    private String desc;
    private Long maxAmount;

    public void convertEntityToDto(CurrencyEntity entity){
        this.currencyId = entity.getCurrencyId();
        this.gameId = entity.getGameId();
        this.name = entity.getName();
        this.desc = entity.getDesc();
        this.maxAmount = entity.getMaxAmount();
        this.createdAt = entity.getCreatedAt();
        this.createdBy = entity.getCreatedBy();
        this.updatedAt = entity.getUpdatedAt();
        this.updatedBy = entity.getUpdatedBy();
        this.isDel = entity.getIsDel();
    }
}
