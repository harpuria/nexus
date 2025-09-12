package com.qwerty.nexus.domain.game.data.currency.dto.request;

import com.qwerty.nexus.domain.game.data.currency.command.CurrencyUpdateCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CurrencyUpdateRequestDto {
    private Integer currencyId;
    private String name;
    private String desc;
    private Long maxAmount;
    private String updatedBy;
    private String isDel;
}
