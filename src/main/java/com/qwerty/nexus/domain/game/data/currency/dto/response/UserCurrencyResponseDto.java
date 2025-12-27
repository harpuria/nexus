package com.qwerty.nexus.domain.game.data.currency.dto.response;

import com.qwerty.nexus.domain.game.data.currency.entity.UserCurrencyEntity;
import com.qwerty.nexus.domain.game.data.currency.result.UserCurrencyListResult;
import com.qwerty.nexus.global.paging.dto.BaseResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class UserCurrencyResponseDto extends BaseResponseDto {
    private Integer userCurrencyId;
    private Integer currencyId;
    private Integer userId;
    private Long amount;

    // 이름이 없으므로 currency table join 결과 반환용 필드 추가
    private String name;

    public static UserCurrencyResponseDto from(UserCurrencyEntity entity){
        return UserCurrencyResponseDto.builder()
                .userCurrencyId(entity.getUserCurrencyId())
                .currencyId(entity.getCurrencyId())
                .userId(entity.getUserId())
                .amount(entity.getAmount())
                .name(entity.getName())
                .build();
    }

    public static UserCurrencyResponseDto from(UserCurrencyListResult result){
        return UserCurrencyResponseDto.builder()
                .amount(result.getAmount())
                .name(result.getName())
                .build();
    }
}
