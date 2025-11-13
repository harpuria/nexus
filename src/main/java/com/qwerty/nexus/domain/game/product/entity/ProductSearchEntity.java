package com.qwerty.nexus.domain.game.product.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductSearchEntity {
    private Integer gameId;
    private String keyword;
    private int offset;
    private int limit;
    @Builder.Default
    private String isDel = "N";
}
