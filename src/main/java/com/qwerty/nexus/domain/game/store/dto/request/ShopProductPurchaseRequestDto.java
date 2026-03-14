package com.qwerty.nexus.domain.game.store.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShopProductPurchaseRequestDto {
    @Positive(message = "shopProductId must be greater than 0.")
    private Integer shopProductId;

    @Positive(message = "userId must be greater than 0.")
    private int userId;
}
