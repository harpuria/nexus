package com.qwerty.nexus.domain.game.data.coupon.entity;

import com.qwerty.nexus.domain.game.data.coupon.TimeLimitType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.jooq.JSONB;

import java.time.OffsetDateTime;

@Getter
@ToString
@Builder
public class CouponEntity {
    private Integer couponId;
    private Integer gameId;
    private String name;
    private String desc;
    private String code;
    private JSONB rewards;
    private TimeLimitType timeLimitType;
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
