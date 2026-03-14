package com.qwerty.nexus.domain.game.store.dto.response;

import com.qwerty.nexus.domain.game.store.entity.StoreProductEntity;
import com.qwerty.nexus.global.dto.BaseResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jooq.JSONB;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class StoreProductResponseDto extends BaseResponseDto {
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

    public static StoreProductResponseDto from(StoreProductEntity entity) {
        return StoreProductResponseDto.builder()
                .shopProductId(entity.getShopProductId())
                .gameId(entity.getGameId())
                .shopId(entity.getShopId())
                .productId(entity.getProductId())
                .sortOrder(entity.getSortOrder())
                .isVisible(entity.getIsVisible())
                .timeLimitType(entity.getTimeLimitType())
                .saleStartAt(entity.getSaleStartAt())
                .saleEndAt(entity.getSaleEndAt())
                .priceType(entity.getPriceType())
                .priceItemCode(entity.getPriceItemCode())
                .priceAmount(entity.getPriceAmount())
                .storeSku(entity.getStoreSku())
                .purchaseLimitType(entity.getPurchaseLimitType())
                .purchaseLimitCount(entity.getPurchaseLimitCount())
                .buyCondition(entity.getBuyCondition())
                .discount(entity.getDiscount())
                .tags(entity.getTags())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .isDel(entity.getIsDel())
                .build();
    }
}
