package com.qwerty.nexus.domain.game.reward.entity;

import com.qwerty.nexus.domain.game.reward.SourceType;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
public class RewardGrantEntity {
    private Integer grantId;
    private Integer gameId;
    private Integer userId;
    private String idempotencyKey;
    private SourceType sourceType;
    private String sourceId;
    private String status;
    private String failCode;
    private String failMessage;
    private Integer itemCount;
    private Long totalAmount;
    private OffsetDateTime createdAt;
    private String createdBy;
    private OffsetDateTime updatedAt;
    private String updatedBy;
    private String isDel;
}
