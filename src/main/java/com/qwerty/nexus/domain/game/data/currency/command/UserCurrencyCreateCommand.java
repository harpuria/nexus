package com.qwerty.nexus.domain.game.data.currency.command;

import com.qwerty.nexus.domain.game.data.currency.dto.request.UserCurrencyCreateRequestDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCurrencyCreateCommand {
    private Integer currencyId;
    private Integer userId;
    private String createdBy;

    public static UserCurrencyCreateCommand from(UserCurrencyCreateRequestDto dto){
        return UserCurrencyCreateCommand.builder()
                .currencyId(dto.getCurrencyId())
                .userId(dto.getUserId())
                .createdBy(dto.getCreatedBy())
                .build();
    }
}
