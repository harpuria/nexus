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
        this.setUserCurrencyId(entity.getUserCurrencyId());
        this.setCurrencyId(entity.getCurrencyId());
        this.setUserId(entity.getUserId());
        this.setAmount(entity.getAmount());
        this.setCreatedAt(entity.getCreatedAt());
        this.setCreatedBy(entity.getCreatedBy());
        this.setUpdatedAt(entity.getUpdatedAt());
        this.setUpdatedBy(entity.getUpdatedBy());
        this.setIsDel(entity.getIsDel());
    }
}
