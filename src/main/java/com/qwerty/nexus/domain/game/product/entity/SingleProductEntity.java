package com.qwerty.nexus.domain.game.product.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
public class SingleProductEntity {
    private Integer singleProductId;
    private Integer productId;
    private Integer currencyId;
    private Long amount;
    private OffsetDateTime createdAt;
    private String createdBy;
    private OffsetDateTime updatedAt;
    private String updatedBy;
    private String isDel;
}
