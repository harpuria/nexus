package com.qwerty.nexus.domain.game.data.coupon.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.OffsetDateTime;

@Getter
@ToString
@Builder
public class CouponUseLogEntity {
    private Integer logId;
    private Integer couponId;
    private Integer userId;
    private OffsetDateTime usedAt;
    private OffsetDateTime createdAt;
    private String createdBy;
    private OffsetDateTime updatedAt;
    private String updatedBy;
    private String isDel;
}
