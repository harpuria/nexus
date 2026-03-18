package com.qwerty.nexus.domain.game.store.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShopProductPurchaseRequestDto {
    @Positive(message = "shopProductId must be greater than 0.")
    @Schema(example = "1")
    private Integer shopProductId;

    @Positive(message = "userId must be greater than 0.")
    @Schema(example = "1")
    private int userId;
}
