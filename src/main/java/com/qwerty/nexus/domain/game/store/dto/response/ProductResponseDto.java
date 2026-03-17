package com.qwerty.nexus.domain.game.store.dto.response;

import com.qwerty.nexus.domain.game.store.entity.ProductEntity;
import com.qwerty.nexus.global.dto.BaseResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class ProductResponseDto extends BaseResponseDto {
    private Integer productId;
    private Integer gameId;
    private String productCode;
    private String name;
    private String desc;
    private String imageUrl;
    private String productType;

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

    public static ProductResponseDto from(ProductEntity productEntity) {
        return ProductResponseDto.builder()
                .productId(productEntity.getProductId())
                .gameId(productEntity.getGameId())
                .productCode(productEntity.getProductCode())
                .name(productEntity.getName())
                .desc(productEntity.getDesc())
                .imageUrl(productEntity.getImageUrl())
                .productType(productEntity.getProductType())
                .shopProductId(productEntity.getShopProductId())
                .shopId(productEntity.getShopId())
                .shopName(productEntity.getShopName())
                .sortOrder(productEntity.getSortOrder())
                .isVisible(productEntity.getIsVisible())
                .timeLimitType(productEntity.getTimeLimitType())
                .saleStartAt(productEntity.getSaleStartAt())
                .saleEndAt(productEntity.getSaleEndAt())
                .priceType(productEntity.getPriceType())
                .priceItemCode(productEntity.getPriceItemCode())
                .priceQty(productEntity.getPriceQty())
                .storeSku(productEntity.getStoreSku())
                .build();
    }
}
