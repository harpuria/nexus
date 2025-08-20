package com.qwerty.nexus.domain.gameData.currency.dto.response;

import com.qwerty.nexus.domain.gameData.currency.entity.CurrencyEntity;
import com.qwerty.nexus.global.dto.BaseResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class CurrencyResponseDto extends BaseResponseDTO {
    private Integer currencyId;
    private Integer gameId;
    private String name;
    private String desc;
    private Long maxAmount;
    private OffsetDateTime createdAt;
    private String createdBy;
    private OffsetDateTime updatedAt;
    private String updatedBy;
    private String isDel;

    public CurrencyResponseDto convertEntityToDTO(CurrencyEntity entity){
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
        return this;
    }
}
