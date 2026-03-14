package com.qwerty.nexus.domain.game.store.entity;

import lombok.Builder;
import lombok.Getter;
import org.jooq.JSONB;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Builder
public class StoreProductEntity {
    private Integer shopProductId;
    private Integer gameId;
    private Integer shopId;
    private Integer productId;
    private Integer sortOrder;
    private String isVisible;
    private String timeLimitType;
    private LocalDateTime saleStartAt;
    private LocalDateTime saleEndAt;
    private String priceType;
    private String priceItemCode;
    private Long priceAmount;
    private String storeSku;
    private String purchaseLimitType;
    private Integer purchaseLimitCount;
    private JSONB buyCondition;
    private JSONB discount;
    private JSONB tags;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private String isDel;
}
