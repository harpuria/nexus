package com.qwerty.nexus.domain.game.store.result;

import lombok.Getter;
import lombok.Setter;
import org.jooq.JSONB;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
public class ProductResult {
    private Integer productId;
    private Integer gameId;
    private String productCode;
    private String name;
    private String desc;
    private String imageUrl;
    private String productType;
    private JSONB rewards;
    private Integer shopProductId;
    private Integer shopId;
    private String shopName;
    private Integer sortOrder;
    private String isVisible;
    private String timeLimitType;
    private LocalDateTime saleStartAt;
    private LocalDateTime saleEndAt;
    private String priceType;
    private String priceItemCode;
    private BigDecimal priceQty;
    private String storeSku;
    private String purchaseLimitType;
    private Integer purchaseLimitCount;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private String isDel;
}
