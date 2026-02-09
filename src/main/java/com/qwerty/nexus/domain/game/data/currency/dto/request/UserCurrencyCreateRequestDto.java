package com.qwerty.nexus.domain.game.data.currency.dto.request;

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
public class UserCurrencyCreateRequestDto {
    @NotNull(message = "currencyId는 필수입니다.")
    @Positive(message = "currencyId는 1 이상이어야 합니다.")
    private Integer currencyId;

    @NotNull(message = "userId는 필수입니다.")
    @Positive(message = "userId는 1 이상이어야 합니다.")
    private Integer userId;

    @NotBlank(message = "createdBy는 필수입니다.")
    @Size(max = 64, message = "createdBy는 64자 이하여야 합니다.")
    private String createdBy;
}
