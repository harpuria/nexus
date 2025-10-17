package com.qwerty.nexus.domain.game.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductInfo {
    private int currencyId;
    private BigDecimal amount;
}
