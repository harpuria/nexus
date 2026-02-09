package com.qwerty.nexus.domain.game.data.currency.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCurrencyOperateRequestDto {
    private Integer userCurrencyId;

    @NotNull(message = "currencyId는 필수입니다.")
    @Positive(message = "currencyId는 1 이상이어야 합니다.")
    private Integer currencyId;

    @NotNull(message = "userId는 필수입니다.")
    @Positive(message = "userId는 1 이상이어야 합니다.")
    private Integer userId;

    @NotBlank(message = "operation은 필수입니다.")
    @Pattern(regexp = "^[+\\-*/]$", message = "operation은 +, -, *, / 중 하나여야 합니다.")
    private String operation;

    @NotNull(message = "operateAmount는 필수입니다.")
    @Positive(message = "operateAmount는 1 이상이어야 합니다.")
    private Long operateAmount;

    private Long currentAmount;

    @NotBlank(message = "updatedBy는 필수입니다.")
    @Size(max = 64, message = "updatedBy는 64자 이하여야 합니다.")
    private String updatedBy;
}
