package com.qwerty.nexus.domain.game.data.currency.dto.request;

import com.qwerty.nexus.domain.game.data.currency.command.UserCurrencyUpdateCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserCurrencyUpdateRequestDto {
    private Integer userCurrencyId;
    private Integer currencyId;
    private Integer userId;
    private Long amount;
    private String updatedBy;
    private String isDel;

    public UserCurrencyUpdateCommand toCommand(){
        return UserCurrencyUpdateCommand.builder()
                .userCurrencyId(this.userCurrencyId)
                .currencyId(this.currencyId)
                .userId(this.userId)
                .amount(this.amount)
                .updatedBy(this.updatedBy)
                .isDel(this.isDel)
                .build();
    }
}
