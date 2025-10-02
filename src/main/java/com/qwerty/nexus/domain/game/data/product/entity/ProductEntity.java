package com.qwerty.nexus.domain.game.data.product.entity;

import com.qwerty.nexus.domain.game.data.product.ProductType;
import com.qwerty.nexus.domain.game.data.product.PurchaseType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Builder
public class ProductEntity {
    private Integer productId;
    private Integer gameId;
    private ProductType productType;
    private PurchaseType purchaseType;
    private Integer currencyId;
    private String name;
    private String desc;
    private BigDecimal price;
    private String createdBy;
    private String updatedBy;
    private String isDel;
}
