package com.qwerty.nexus.domain.game.data.currency.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCurrencyCreateCommand {
    private Integer currencyId;
    private Integer userId;
    private String createdBy;
}
