package com.qwerty.nexus.domain.game.data.coupon.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UseCouponRequestDto {
    @NotNull(message = "gameId는 필수입니다.")
    @Positive(message = "gameId는 1 이상이어야 합니다.")
    private Integer gameId;

    @NotNull(message = "userId는 필수입니다.")
    @Positive(message = "userId는 1 이상이어야 합니다.")
    private Integer userId;

    @NotBlank(message = "couponCode는 필수입니다.")
    @Size(max = 255, message = "couponCode는 255자 이하여야 합니다.")
    private String couponCode;
}
