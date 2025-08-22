package com.qwerty.nexus.domain.game.data.currency.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CurrencyCreateCommand {
    private Integer gameId;
    private String name;
    private String desc;
    private Long maxAmount;
    private String createdBy;
}
