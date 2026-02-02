package com.qwerty.nexus.domain.game.product.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductBuyRequestDto {
    @Positive(message = "상품 ID는 1 이상이어야 합니다.")
    private int productId;

    @Positive(message = "유저 ID는 1 이상이어야 합니다.")
    private int userId;
}
