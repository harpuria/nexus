package com.qwerty.nexus.domain.game.coupon.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CouponRewardInfo {
    private int currencyId;
    private long amount;
}
