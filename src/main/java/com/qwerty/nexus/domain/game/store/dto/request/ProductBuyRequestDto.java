package com.qwerty.nexus.domain.game.store.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductBuyRequestDto {
    @Positive(message = "상품 ID는 1 이상이어야 합니다.")
    @Schema(example = "1")
    private int productId;

    @Positive(message = "유저 ID는 1 이상이어야 합니다.")
    @Schema(example = "1")
    private int userId;
}
