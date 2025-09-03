package com.qwerty.nexus.domain.game.data.currency.command;

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
}
