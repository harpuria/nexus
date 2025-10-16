package com.qwerty.nexus.domain.game.product.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductBuyRequestDto {
    private int productId;
    private int userId;
}
