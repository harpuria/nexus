package com.qwerty.nexus.domain.game.shop.entity;

import lombok.Builder;
import lombok.Getter;
import org.jooq.JSONB;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Builder
public class ShopEntity {
    private Integer shopId;
    private Integer gameId;
    private String shopCode;
    private String name;
    private String desc;
    private String shopType;
    private String timeLimitType;
    private LocalDateTime openAt;
    private LocalDateTime closeAt;
    private JSONB openCondition;
    private Integer sortOrder;
    private String isVisible;
    private OffsetDateTime createdAt;
    private String createdBy;
    private OffsetDateTime updatedAt;
    private String updatedBy;
    private String isDel;
}
