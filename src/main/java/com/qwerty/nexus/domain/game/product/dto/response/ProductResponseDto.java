package com.qwerty.nexus.domain.game.product.dto.response;

import com.qwerty.nexus.domain.game.product.PurchaseType;
import com.qwerty.nexus.domain.game.product.entity.ProductEntity;
import com.qwerty.nexus.global.dto.BaseResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;

@Getter
@SuperBuilder
public class ProductResponseDto extends BaseResponseDto {
    private Integer productId;
    private Integer gameId;
    private PurchaseType purchaseType;
    private Integer itemId;
    private String name;
    private String desc;
    private BigDecimal price;

    public static ProductResponseDto from(ProductEntity productEntity) {
        return ProductResponseDto.builder()
                .productId(productEntity.getProductId())
                .gameId(productEntity.getGameId())
                .purchaseType(productEntity.getPurchaseType())
                .itemId(productEntity.getItemId())
                .name(productEntity.getName())
                .desc(productEntity.getDesc())
                .price(productEntity.getPrice())
                .build();
    }
}
