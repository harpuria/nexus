package com.qwerty.nexus.domain.game.data.item.result;

import lombok.Getter;
import lombok.Setter;
import org.jooq.JSONB;

@Getter
@Setter
public class UserItemInstanceListResult {
    private Integer userItemId;
    private String itemName;
    private JSONB stateJson;
}
