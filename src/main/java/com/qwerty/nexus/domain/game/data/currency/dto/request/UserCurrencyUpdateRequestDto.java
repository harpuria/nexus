package com.qwerty.nexus.domain.game.data.currency.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCurrencyUpdateRequestDto {
    private Integer userCurrencyId;
    private Integer currencyId;
    private Integer userId;

    @PositiveOrZero(message = "amount는 0 이상이어야 합니다.")
    private Long amount;

    @Size(max = 64, message = "updatedBy는 64자 이하여야 합니다.")
    private String updatedBy;

    @Pattern(regexp = "^[YNyn]$", message = "isDel은 Y 또는 N 이어야 합니다.")
    private String isDel;
}
