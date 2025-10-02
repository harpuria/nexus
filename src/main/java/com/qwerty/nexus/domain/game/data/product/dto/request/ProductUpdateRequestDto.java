package com.qwerty.nexus.domain.game.data.product.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qwerty.nexus.domain.game.data.product.ProductType;
import com.qwerty.nexus.domain.game.data.product.PurchaseType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductUpdateRequestDto {
    private Integer gameId;
    private ProductType productType;
    private PurchaseType purchaseType;
    private Integer currencyId;
    private String name;
    private String desc;
    private BigDecimal price;
    private String updatedBy;
    private String isDel;

    // no parameter
    @JsonIgnore
    private Integer productId;
}
