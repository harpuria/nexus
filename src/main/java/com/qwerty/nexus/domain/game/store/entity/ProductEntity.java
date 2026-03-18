package com.qwerty.nexus.domain.game.store.entity;

import lombok.Builder;
import lombok.Getter;
import org.jooq.JSONB;

import java.time.OffsetDateTime;

@Getter
@Builder
public class ProductEntity {
    private Integer productId;
    private Integer gameId;
    private String productCode;
    private String name;
    private String desc;
    private String imageUrl;
    private String productType;
    private JSONB rewards;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private String isDel;
}
