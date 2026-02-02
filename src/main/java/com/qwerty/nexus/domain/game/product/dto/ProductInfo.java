package com.qwerty.nexus.domain.game.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductInfo {
    private int currencyId;
    private Long amount;
    private Long maxAmount;
}
