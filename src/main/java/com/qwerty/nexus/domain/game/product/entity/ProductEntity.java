package com.qwerty.nexus.domain.game.product.entity;

import com.qwerty.nexus.domain.game.product.PurchaseType;
import lombok.Builder;
import lombok.Getter;
import org.jooq.JSONB;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Builder
public class ProductEntity {
    private Integer productId;
    private Integer gameId;
    private PurchaseType purchaseType;
    private Integer currencyId;
    private String name;
    private String desc;
    private BigDecimal price;
    private JSONB rewards;
    private String limitType;
    private OffsetDateTime availableStart;
    private OffsetDateTime availableEnd;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private String isDel;
}
