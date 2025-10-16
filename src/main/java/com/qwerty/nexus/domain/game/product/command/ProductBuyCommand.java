package com.qwerty.nexus.domain.game.product.command;

import com.qwerty.nexus.domain.game.product.dto.request.ProductBuyRequestDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductBuyCommand {
    private int productId;
    private int userId;

    public static ProductBuyCommand from(ProductBuyRequestDto dto){
        return ProductBuyCommand.builder()
                .productId(dto.getProductId())
                .userId(dto.getUserId())
                .build();
    }
}
