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
    @Positive(message = "shopId는 1 이상이어야 합니다.")
    @Schema(example = "1")
    private Integer shopId;

    @Size(max = 100, message = "shopCode는 100자 이하여야 합니다.")
    @Schema(example = "TEST_CODE_001")
    private String shopCode;

    @Size(max = 255, message = "name은 255자 이하여야 합니다.")
    @Schema(example = "테스트 데이터")
    private String name;

    @Schema(example = "예시 설명입니다.")
    private String desc;

    @Size(max = 32, message = "shopType은 32자 이하여야 합니다.")
    @Schema(example = "NORMAL")
    private String shopType;

    @Size(max = 32, message = "timeLimitType은 32자 이하여야 합니다.")
    @Schema(example = "LIMITED")
    private String timeLimitType;

    @Schema(example = "2026-03-18T09:00:00")
    private LocalDateTime openAt;

    @Schema(example = "2026-03-18T09:00:00")
    private LocalDateTime closeAt;

    @Schema(example = "{\"key\":\"value\"}")
    private JSONB openCondition;

    @Schema(example = "1")
    private Integer sortOrder;

    @Size(max = 1, message = "isVisible은 1자여야 합니다.")
    @Schema(example = "N")
    private String isVisible;

    @Size(max = 1, message = "isDel은 1자여야 합니다.")
    @Schema(example = "N")
    private String isDel;

    @NotBlank(message = "updatedBy는 필수입니다.")
    @Size(max = 64, message = "updatedBy는 64자 이하여야 합니다.")
    @Schema(example = "admin")
    private String updatedBy;
}
