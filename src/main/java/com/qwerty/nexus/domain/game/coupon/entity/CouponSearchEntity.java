package com.qwerty.nexus.domain.game.coupon.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
public class CouponSearchEntity {
    private Integer gameId;
    private String code;
    private String keyword;
    private String isDel;
    private OffsetDateTime startDateFrom;
    private OffsetDateTime startDateTo;
    private OffsetDateTime endDateFrom;
    private OffsetDateTime endDateTo;
    private OffsetDateTime createdFrom;
    private OffsetDateTime createdTo;
    private Integer limit;
    private Integer offset;
    private String sort;
    private String direction;
}
