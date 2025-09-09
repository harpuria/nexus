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
        this.setCurrencyId(entity.getCurrencyId());
        this.setGameId(entity.getGameId());
        this.setName(entity.getName());
        this.setDesc(entity.getDesc());
        this.setMaxAmount(entity.getMaxAmount());
        this.setCreatedAt(entity.getCreatedAt());
        this.setCreatedBy(entity.getCreatedBy());
        this.setUpdatedAt(entity.getUpdatedAt());
        this.setUpdatedBy(entity.getUpdatedBy());
        this.setIsDel(entity.getIsDel());
    }
}
