package com.qwerty.nexus.domain.game.data.coupon.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder(toBuilder = true)
public class CouponListResponseDto {
    private List<CouponResponseDto> coupons;
    private int page;
    private int size;
    private long totalCount;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
}
