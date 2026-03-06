package com.qwerty.nexus.domain.game.item.entity;

import lombok.Builder;
import lombok.Getter;
import org.jooq.JSONB;

import java.time.OffsetDateTime;

@Getter
@Builder
public class ItemEntity {
    private Integer itemId;
    private Integer gameId;
    private String itemCode;
    private String name;
    private String desc;
    private String itemType;
    private String isStackable;
    private Long defaultStack;
    private Long maxStack;
    private String rarity;
    private String iconPath;
    private JSONB metaJson;
    private OffsetDateTime createdAt;
    private String createdBy;
    private OffsetDateTime updatedAt;
    private String updatedBy;
    private String isDel;
}
