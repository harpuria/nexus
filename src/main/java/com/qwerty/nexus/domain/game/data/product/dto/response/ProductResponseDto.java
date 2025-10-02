package com.qwerty.nexus.domain.game.data.product.dto.response;

import com.qwerty.nexus.domain.game.data.product.ProductType;
import com.qwerty.nexus.domain.game.data.product.PurchaseType;
import com.qwerty.nexus.domain.game.data.product.entity.ProductEntity;
import com.qwerty.nexus.global.extend.dto.BaseResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;

@Getter
@SuperBuilder
public class ProductResponseDto extends BaseResponseDto {
    private Integer productId;
    private Integer gameId;
    private ProductType productType;
    private PurchaseType purchaseType;
    private Integer currencyId;
    private String name;
    private String desc;
    private BigDecimal price;

    public static ProductResponseDto from(ProductEntity productEntity) {
        return ProductResponseDto.builder()
                .productId(productEntity.getProductId())
                .gameId(productEntity.getGameId())
                .productType(productEntity.getProductType())
                .purchaseType(productEntity.getPurchaseType())
                .currencyId(productEntity.getCurrencyId())
                .name(productEntity.getName())
                .desc(productEntity.getDesc())
                .price(productEntity.getPrice())
                .build();
    }
}
