package com.qwerty.nexus.domain.game.data.currency.dto.request;

import com.qwerty.nexus.domain.game.data.currency.command.UserCurrencyCreateCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCurrencyCreateRequestDto {
    private Integer currencyId;
    private Integer userId;
    private String createdBy;

    public UserCurrencyCreateCommand toCommand(){
        return UserCurrencyCreateCommand.builder()
                .currencyId(this.currencyId)
                .userId(this.userId)
                .createdBy(this.createdBy)
                .build();
    }
}
