package com.qwerty.nexus.domain.game.data.currency.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCurrencyOperateRequestDto {
    private Integer userCurrencyId;
    private Integer currencyId;
    private Integer userId;
    private String operation;   // 연산
    private Long operateAmount; // 연산값
    private Long currentAmount; // 현재재화값 (DB 비교)
    private String updatedBy;
}
