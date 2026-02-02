package com.qwerty.nexus.domain.game.product.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qwerty.nexus.domain.game.product.PurchaseType;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductUpdateRequestDto {
    private Integer gameId;
    private PurchaseType purchaseType;
    private Integer currencyId;

    @Size(max = 255, message = "상품명은 255자 이하여야 합니다.")
    private String name;

    private String desc;

    @Positive(message = "가격은 0보다 커야 합니다.")
    private BigDecimal price;

    @Size(max = 64, message = "updatedBy는 64자 이하여야 합니다.")
    private String updatedBy;

    @Pattern(regexp = "^[YN]$", message = "isDel 값은 Y 또는 N 이어야 합니다.")
    private String isDel;

    // no parameter
    @JsonIgnore
    private Integer productId;
}
