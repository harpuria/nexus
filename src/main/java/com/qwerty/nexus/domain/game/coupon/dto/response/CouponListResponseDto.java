package com.qwerty.nexus.domain.game.coupon.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record CouponListResponseDto(
        List<CouponResponseDto> coupons,
        int page,
        int size,
        long totalCount,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious) {
}
