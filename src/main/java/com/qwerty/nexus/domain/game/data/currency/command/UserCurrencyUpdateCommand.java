package com.qwerty.nexus.domain.game.data.currency.command;

import com.qwerty.nexus.domain.game.data.currency.dto.request.UserCurrencyUpdateRequestDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCurrencyUpdateCommand {
    private Integer userCurrencyId;
    private Integer currencyId;
    private Integer userId;
    private Long amount;
    private String updatedBy;
    private String isDel;

    public static UserCurrencyUpdateCommand from(UserCurrencyUpdateRequestDto dto){
        return UserCurrencyUpdateCommand.builder()
                .userCurrencyId(dto.getUserCurrencyId())
                .currencyId(dto.getCurrencyId())
                .userId(dto.getUserId())
                .amount(dto.getAmount())
                .updatedBy(dto.getUpdatedBy())
                .isDel(dto.getIsDel())
                .build();
    }
}
