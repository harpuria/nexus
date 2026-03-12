package com.qwerty.nexus.domain.game.store.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
public class ShopUpdateRequestDto {
    @Schema(hidden = true)
    @Positive(message = "shopId는 1 이상이어야 합니다.")
    private Integer shopId;

    @Schema(example = "MAIN_SHOP")
    @Size(max = 100, message = "shopCode는 100자 이하여야 합니다.")
    private String shopCode;

    @Schema(example = "메인 상점")
    @Size(max = 255, message = "name은 255자 이하여야 합니다.")
    private String name;

    private String desc;

    @Schema(example = "NORMAL")
    @Size(max = 32, message = "shopType은 32자 이하여야 합니다.")
    private String shopType;

    @Schema(example = "ALWAYS")
    @Size(max = 32, message = "timeLimitType은 32자 이하여야 합니다.")
    private String timeLimitType;

    private LocalDateTime openAt;

    private LocalDateTime closeAt;

    private JSONB openCondition;

    private Integer sortOrder;

    @Schema(example = "Y")
    @Size(max = 1, message = "isVisible은 1자여야 합니다.")
    private String isVisible;

    @Schema(example = "N")
    @Size(max = 1, message = "isDel은 1자여야 합니다.")
    private String isDel;

    @NotBlank(message = "updatedBy는 필수입니다.")
    @Size(max = 64, message = "updatedBy는 64자 이하여야 합니다.")
    private String updatedBy;
}
