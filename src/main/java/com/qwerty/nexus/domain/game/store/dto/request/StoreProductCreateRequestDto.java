package com.qwerty.nexus.domain.game.store.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jooq.JSONB;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class StoreProductCreateRequestDto {
    @NotNull(message = "gameId는 필수입니다.")
    @Positive(message = "gameId는 1 이상이어야 합니다.")
    @Schema(example = "1")
    private Integer gameId;

    @NotNull(message = "shopId는 필수입니다.")
    @Positive(message = "shopId는 1 이상이어야 합니다.")
    @Schema(example = "1")
    private Integer shopId;

    @NotNull(message = "productId는 필수입니다.")
    @Positive(message = "productId는 1 이상이어야 합니다.")
    @Schema(example = "1")
    private Integer productId;

    @PositiveOrZero(message = "sortOrder는 0 이상이어야 합니다.")
    @Schema(example = "1")
    private Integer sortOrder;

    @Pattern(regexp = "^[YN]$", message = "isVisible 값은 Y 또는 N 이어야 합니다.")
    @Schema(example = "N")
    private String isVisible;

    @Size(max = 16, message = "timeLimitType은 16자 이하여야 합니다.")
    @Schema(example = "LIMITED")
    private String timeLimitType;

    @Schema(example = "2026-03-18T09:00:00")
    private LocalDateTime saleStartAt;

    @Schema(example = "2026-03-18T09:00:00")
    private LocalDateTime saleEndAt;

    @Size(max = 16, message = "priceType은 16자 이하여야 합니다.")
    @Schema(example = "sample")
    private String priceType;

    @Size(max = 64, message = "priceItemCode는 64자 이하여야 합니다.")
    @Schema(example = "TEST_CODE_001")
    private String priceItemCode;

    @PositiveOrZero(message = "priceQty는 0 이상이어야 합니다.")
    @Schema(example = "100")
    private Long priceQty;

    @Size(max = 128, message = "storeSku는 128자 이하여야 합니다.")
    @Schema(example = "sample")
    private String storeSku;

    @Size(max = 16, message = "purchaseLimitType은 16자 이하여야 합니다.")
    @Schema(example = "sample")
    private String purchaseLimitType;

    @PositiveOrZero(message = "purchaseLimitCount는 0 이상이어야 합니다.")
    @Schema(example = "100")
    private Integer purchaseLimitCount;

    @Schema(example = "{\"key\":\"value\"}")
    private JSONB buyCondition;

    @Schema(example = "{\"key\":\"value\"}")
    private JSONB discount;

    @Schema(example = "{\"key\":\"value\"}")
    private JSONB tags;

    @NotBlank(message = "createdBy는 필수입니다.")
    @Size(max = 64, message = "createdBy는 64자 이하여야 합니다.")
    @Schema(example = "admin")
    private String createdBy;
}
