package com.qwerty.nexus.domain.game.store.product.dto.response;

import com.qwerty.nexus.domain.game.store.product.entity.ProductEntity;
import com.qwerty.nexus.domain.reward.dto.RewardDto;
import com.qwerty.nexus.global.dto.BaseResponseDto;
import com.qwerty.nexus.global.util.CommonUtil;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@SuperBuilder
public class ProductDetailResponseDto extends BaseResponseDto {
    private Integer productId;
    private Integer gameId;
    private String productCode;
    private String name;
    private String desc;
    private String imageUrl;
    private String productType;
    private List<RewardDto> rewards;

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
    private BigDecimal priceAmount;
    private String storeSku;
    private String purchaseLimitType;
    private Integer purchaseLimitCount;

    public static ProductDetailResponseDto from(ProductEntity entity) {
        return ProductDetailResponseDto.builder()
                .productId(entity.getProductId())
                .gameId(entity.getGameId())
                .productCode(entity.getProductCode())
                .name(entity.getName())
                .desc(entity.getDesc())
                .imageUrl(entity.getImageUrl())
                .productType(entity.getProductType())
                .rewards(CommonUtil.jsonbToDto(entity.getRewards(), RewardDto.class))
                .shopProductId(entity.getShopProductId())
                .shopId(entity.getShopId())
                .shopName(entity.getShopName())
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
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .isDel(entity.getIsDel())
                .build();
    }
}
