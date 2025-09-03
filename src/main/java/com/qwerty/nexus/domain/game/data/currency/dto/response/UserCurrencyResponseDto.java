package com.qwerty.nexus.domain.game.data.currency.dto.response;

import com.qwerty.nexus.domain.game.data.currency.entity.UserCurrencyEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class UserCurrencyResponseDto {
    private Integer userCurrencyId;
    private Integer currencyId;
    private Integer userId;
    private Long amount;
    private OffsetDateTime createdAt;
    private String createdBy;
    private OffsetDateTime updatedAt;
    private String updatedBy;
    private String isDel;

    public void convertToDto(UserCurrencyEntity entity){
        this.userCurrencyId = entity.getUserCurrencyId();
        this.currencyId = entity.getCurrencyId();
        this.userId = entity.getUserId();
        this.amount = entity.getAmount();
        this.createdAt = entity.getCreatedAt();
        this.createdBy = entity.getCreatedBy();
        this.updatedAt = entity.getUpdatedAt();
        this.updatedBy = entity.getUpdatedBy();
        this.isDel = entity.getIsDel();
    }
}
