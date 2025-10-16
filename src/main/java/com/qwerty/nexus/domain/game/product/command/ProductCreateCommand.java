package com.qwerty.nexus.domain.game.product.command;

import com.qwerty.nexus.domain.game.product.PurchaseType;
import com.qwerty.nexus.domain.game.product.dto.request.ProductCreateRequestDto;
import com.qwerty.nexus.domain.game.product.dto.request.ProductInfo;
import lombok.Builder;
import lombok.Getter;
import org.jooq.JSONB;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Builder
public class ProductCreateCommand {
    private Integer gameId;
    private PurchaseType purchaseType;
    private Integer currencyId;
    private String name;
    private String desc;
    private BigDecimal price;
    private JSONB rewards;      // ex) [{"currencyId" : 1, "amount" : 100}]
    private String limitType;
    private OffsetDateTime availableStart;
    private OffsetDateTime availableEnd;
    private String createdBy;
    private String updatedBy;

    public static ProductCreateCommand from(ProductCreateRequestDto dto){
        return ProductCreateCommand.builder()
                .gameId(dto.getGameId())
                .purchaseType(dto.getPurchaseType())
                .currencyId(dto.getCurrencyId())
                .name(dto.getName())
                .desc(dto.getDesc())
                .price(dto.getPrice())
                .rewards(dto.getRewards())
                .limitType(dto.getLimitType())
                .availableStart(dto.getAvailableStart())
                .availableEnd(dto.getAvailableEnd())
                .createdBy(dto.getCreatedBy())
                .updatedBy(dto.getUpdatedBy())
                .build();
    }
}
