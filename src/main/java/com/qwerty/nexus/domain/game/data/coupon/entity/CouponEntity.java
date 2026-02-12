package com.qwerty.nexus.domain.game.data.coupon.entity;

import lombok.Builder;
import lombok.Getter;
import org.jooq.JSONB;

import java.time.OffsetDateTime;

@Getter
@Builder
public class CouponEntity {
    private Integer couponId;
    private Integer gameId;
    private String name;
    private String desc;
    private String code;
    private JSONB rewards;
    private String timeLimitType;
    private OffsetDateTime useStartDate;
    private OffsetDateTime useEndDate;
    private Long maxIssueCount;
    private Integer useLimitPerUser;
    private OffsetDateTime createdAt;
    private String createdBy;
    private OffsetDateTime updatedAt;
    private String updatedBy;
    private String isDel;
}
