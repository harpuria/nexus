package com.qwerty.nexus.domain.game.product.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductSearchCommand {
    private Integer gameId;
    private int page;
    private int size;
    private String keyword;
}
