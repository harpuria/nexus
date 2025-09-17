package com.qwerty.nexus.domain.game.data.product.command;

import com.qwerty.nexus.domain.game.data.product.dto.request.ProductCreateRequestDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductCreateCommand {
    public static ProductCreateCommand from(ProductCreateRequestDto dto){
        return ProductCreateCommand.builder().build();
    }
}
