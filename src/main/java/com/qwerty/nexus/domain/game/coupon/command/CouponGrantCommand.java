package com.qwerty.nexus.domain.game.coupon.command;

import com.qwerty.nexus.domain.game.coupon.dto.request.CouponGrantRequestDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CouponGrantCommand {
    private String code;
    private Integer userId;
    private String requestedBy;

    public static CouponGrantCommand from(CouponGrantRequestDto dto) {
        return CouponGrantCommand.builder()
                .code(dto.getCode())
                .userId(dto.getUserId())
                .requestedBy(dto.getRequestedBy())
                .build();
    }
}
