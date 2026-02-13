package com.qwerty.nexus.domain.game.data.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CouponRewardInfo {
    private int currencyId;
    private Long amount;
}
