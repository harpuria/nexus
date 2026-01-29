package com.qwerty.nexus.domain.game.product.dto.response;

import com.qwerty.nexus.domain.game.product.LimitType;
import com.qwerty.nexus.domain.game.product.PurchaseType;
import com.qwerty.nexus.domain.game.product.entity.ProductEntity;
import com.qwerty.nexus.global.paging.dto.BaseResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jooq.JSONB;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@SuperBuilder
public class ProductDetailResponseDto extends BaseResponseDto {
    private Integer productId;
    private Integer gameId;
    private PurchaseType purchaseType;
    private Integer currencyId;
    private String name;
    private String desc;
    private BigDecimal price;
    private JSONB rewards;
    private LimitType limitType;
    private OffsetDateTime availableStart;
    private OffsetDateTime availableEnd;

    public static ProductDetailResponseDto from(ProductEntity entity) {
        return ProductDetailResponseDto.builder()
                .productId(entity.getProductId())
                .gameId(entity.getGameId())
                .purchaseType(entity.getPurchaseType())
                .currencyId(entity.getCurrencyId())
                .name(entity.getName())
                .desc(entity.getDesc())
                .price(entity.getPrice())
                .rewards(entity.getRewards())
                .limitType(entity.getLimitType())
                .availableStart(entity.getAvailableStart())
                .availableEnd(entity.getAvailableEnd())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .isDel(entity.getIsDel())
                .build();
    }
}
