package com.qwerty.nexus.domain.game.product.command;

import com.qwerty.nexus.domain.game.product.ProductType;
import com.qwerty.nexus.domain.game.product.PurchaseType;
import com.qwerty.nexus.domain.game.product.dto.request.ProductCreateRequestDto;
import com.qwerty.nexus.domain.game.product.dto.request.ProductInfo;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class ProductCreateCommand {
    private Integer gameId;
    private ProductType productType;
    private PurchaseType purchaseType;
    private Integer currencyId;
    private String name;
    private String desc;
    private BigDecimal price;
    private String createdBy;
    private String updatedBy;

    private List<ProductInfo> productInfoList;

    public static ProductCreateCommand from(ProductCreateRequestDto dto){
        return ProductCreateCommand.builder()
                .gameId(dto.getGameId())
                .productType(dto.getProductType())
                .purchaseType(dto.getPurchaseType())
                .currencyId(dto.getCurrencyId())
                .name(dto.getName())
                .desc(dto.getDesc())
                .price(dto.getPrice())
                .createdBy(dto.getCreatedBy())
                .updatedBy(dto.getUpdatedBy())
                .productInfoList(dto.getProductInfoList())
                .build();
    }
}
