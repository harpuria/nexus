package com.qwerty.nexus.domain.game.product.command;

import com.qwerty.nexus.domain.game.product.PurchaseType;
import com.qwerty.nexus.domain.game.product.dto.request.ProductUpdateRequestDto;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ProductUpdateCommand {
    private Integer gameId;
    private PurchaseType purchaseType;
    private Integer currencyId;
    private String name;
    private String desc;
    private BigDecimal price;
    private String updatedBy;
    private String isDel;

    public static ProductUpdateCommand from(ProductUpdateRequestDto dto){
        return ProductUpdateCommand.builder()
                .gameId(dto.getGameId())
                .purchaseType(dto.getPurchaseType())
                .currencyId(dto.getCurrencyId())
                .name(dto.getName())
                .desc(dto.getDesc())
                .price(dto.getPrice())
                .updatedBy(dto.getUpdatedBy())
                .isDel(dto.getIsDel())
                .build();
    }
}
