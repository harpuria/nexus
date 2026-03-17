package com.qwerty.nexus.domain.game.item.result;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserItemStackListResult {
    private Integer userItemStackId;
    private String itemName;
    private Long qty;
}
