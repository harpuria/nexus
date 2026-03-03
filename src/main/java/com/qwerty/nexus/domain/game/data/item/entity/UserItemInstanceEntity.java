package com.qwerty.nexus.domain.game.data.item.entity;

import lombok.Builder;
import lombok.Getter;
import org.jooq.JSONB;

import java.time.OffsetDateTime;

@Getter
@Builder
public class UserItemInstanceEntity {
    private Integer userItemId;
    private Integer userId;
    private Integer itemId;
    private JSONB stateJson;
    private OffsetDateTime acquiredAt;
    private OffsetDateTime createdAt;
    private String createdBy;
    private OffsetDateTime updatedAt;
    private String updatedBy;
    private String isDel;
}
