package com.qwerty.nexus.domain.game.store.shop.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jooq.JSONB;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ShopCreateRequestDto {
    @Schema(example = "1")
    @NotNull(message = "gameId는 필수입니다.")
    @Positive(message = "gameId는 1 이상이어야 합니다.")
    private Integer gameId;

    @Schema(example = "MAIN_SHOP")
    @NotBlank(message = "shopCode는 필수입니다.")
    @Size(max = 100, message = "shopCode는 100자 이하여야 합니다.")
    private String shopCode;

    @Schema(example = "메인 상점")
    @NotBlank(message = "name은 필수입니다.")
    @Size(max = 255, message = "name은 255자 이하여야 합니다.")
    private String name;

    @Schema(example = "기본 재화를 구매할 수 있는 상점")
    private String desc;

    @Schema(example = "NORMAL")
    @NotBlank(message = "shopType은 필수입니다.")
    @Size(max = 32, message = "shopType은 32자 이하여야 합니다.")
    private String shopType;

    @Schema(example = "ALWAYS")
    @NotBlank(message = "timeLimitType은 필수입니다.")
    @Size(max = 32, message = "timeLimitType은 32자 이하여야 합니다.")
    private String timeLimitType;

    private LocalDateTime openAt;

    private LocalDateTime closeAt;

    private JSONB openCondition;

    private Integer sortOrder;

    @Schema(example = "Y")
    @Size(max = 1, message = "isVisible은 1자여야 합니다.")
    private String isVisible;

    @NotBlank(message = "createdBy는 필수입니다.")
    @Size(max = 64, message = "createdBy는 64자 이하여야 합니다.")
    private String createdBy;
}
