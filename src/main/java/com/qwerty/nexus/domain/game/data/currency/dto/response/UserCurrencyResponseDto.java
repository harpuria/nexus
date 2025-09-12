package com.qwerty.nexus.domain.game.data.currency.dto.response;

import com.qwerty.nexus.domain.game.data.currency.entity.UserCurrencyEntity;
import com.qwerty.nexus.global.extend.dto.BaseResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class UserCurrencyResponseDto extends BaseResponseDto {
    private Integer userCurrencyId;
    private Integer currencyId;
    private Integer userId;
    private Long amount;

    public static UserCurrencyResponseDto from(UserCurrencyEntity entity){
        return UserCurrencyResponseDto.builder()
                .userCurrencyId(entity.getUserCurrencyId())
                .currencyId(entity.getCurrencyId())
                .userId(entity.getUserId())
                .amount(entity.getAmount())
                .build();
    }
}
