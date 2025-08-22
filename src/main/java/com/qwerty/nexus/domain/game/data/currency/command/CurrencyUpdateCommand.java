package com.qwerty.nexus.domain.game.data.currency.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CurrencyUpdateCommand {
    private Integer currencyId;
    private String name;
    private String desc;
    private Long maxAmount;
    private String updatedBy;
    private String isDel;
}
