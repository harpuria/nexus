package com.qwerty.nexus.domain.game.reward.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
public class RewardGrantItemEntity {
    private Integer grantItemId;
    private Integer grantId;
    private Integer itemId;
    private String isStackable;
    private Long amount;
    private String itemCode;
    private String itemType;
    private String status;
    private String failReason;
    private OffsetDateTime createdAt;
    private String createdBy;
    private OffsetDateTime updatedAt;
    private String updatedBy;
    private String isDel;
}
