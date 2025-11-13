package com.qwerty.nexus.domain.game.coupon.command;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
public class CouponSearchCommand {
    private Integer page;
    private Integer size;
    private String sort;
    private String direction;
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
}
