package com.qwerty.nexus.domain.game.item.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
public class UserItemStackEntity {
    private Integer userItemStackId;
    private Integer userId;
    private Integer itemId;
    private Long amount;
    private OffsetDateTime createdAt;
    private String createdBy;
    private OffsetDateTime updatedAt;
    private String updatedBy;
    private String isDel;
}
