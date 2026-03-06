package com.qwerty.nexus.domain.game.reward.entity;

import lombok.Builder;
import lombok.Getter;
import org.jooq.JSONB;

import java.time.OffsetDateTime;

@Getter
@Builder
public class RewardGrantItemEntity {
    private Integer grantItemId;
    private Integer grantId;
    private Integer gameId;
    private Integer userId;
    private String itemCode;
    private Integer itemId;
    private Long amount;
    private String resultType;
    private JSONB resultMeta;
    private OffsetDateTime createdAt;
    private String createdBy;
    private OffsetDateTime updatedAt;
    private String updatedBy;
    private String isDel;
}
