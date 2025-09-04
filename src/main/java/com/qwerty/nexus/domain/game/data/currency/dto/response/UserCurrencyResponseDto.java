package com.qwerty.nexus.domain.game.data.currency.dto.response;

import com.qwerty.nexus.domain.game.data.currency.entity.UserCurrencyEntity;
import com.qwerty.nexus.global.extend.dto.BaseResponseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCurrencyResponseDto extends BaseResponseDto {
    private Integer userCurrencyId;
    private Integer currencyId;
    private Integer userId;
    private Long amount;

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
