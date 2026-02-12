package com.qwerty.nexus.domain.game.data.currency.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CurrencyCreateRequestDto {
    @Schema(example = "1")
    @NotNull(message = "gameId는 필수입니다.")
    @Positive(message = "gameId는 1 이상이어야 합니다.")
    private Integer gameId;

    @Schema(example = "골드")
    @NotBlank(message = "재화 이름은 필수입니다.")
    @Size(max = 255, message = "재화 이름은 255자 이하여야 합니다.")
    private String name;

    @Schema(example = "인게임에서 쓰는 골드 재화")
    @NotBlank(message = "재화 설명은 필수입니다.")
    private String desc;

    @Schema(example = "1000000000")
    @PositiveOrZero(message = "기본 수량은 0 이상이어야 합니다.")
    private Long defaultAmount;

    @Schema(example = "1000000000")
    @NotNull(message = "최대 수량은 필수입니다.")
    @PositiveOrZero(message = "최대 수량은 0 이상이어야 합니다.")
    private Long maxAmount;

    @Schema(example = "admin")
    @NotBlank(message = "createdBy는 필수입니다.")
    @Size(max = 64, message = "createdBy는 64자 이하여야 합니다.")
    private String createdBy;
}
