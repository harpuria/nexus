package com.qwerty.nexus.domain.game.coupon.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CouponGrantRequestDto {
    private Integer userId;
    private String requestedBy;
}
