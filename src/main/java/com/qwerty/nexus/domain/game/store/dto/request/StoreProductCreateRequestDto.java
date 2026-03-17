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
    @Schema(example = "1")
    @NotNull(message = "gameId는 필수입니다.")
    @Positive(message = "gameId는 1 이상이어야 합니다.")
    private Integer gameId;

    @Schema(example = "10")
    @NotNull(message = "shopId는 필수입니다.")
    @Positive(message = "shopId는 1 이상이어야 합니다.")
    private Integer shopId;

    @Schema(example = "100")
    @NotNull(message = "productId는 필수입니다.")
    @Positive(message = "productId는 1 이상이어야 합니다.")
    private Integer productId;

    @Schema(example = "1")
    @PositiveOrZero(message = "sortOrder는 0 이상이어야 합니다.")
    private Integer sortOrder;

    @Schema(example = "Y")
    @Pattern(regexp = "^[YN]$", message = "isVisible 값은 Y 또는 N 이어야 합니다.")
    private String isVisible;

    @Schema(example = "UNLIMITED")
    @Size(max = 16, message = "timeLimitType은 16자 이하여야 합니다.")
    private String timeLimitType;

    private LocalDateTime saleStartAt;

    private LocalDateTime saleEndAt;

    @Schema(example = "SOFT")
    @Size(max = 16, message = "priceType은 16자 이하여야 합니다.")
    private String priceType;

    @Schema(example = "GEM")
    @Size(max = 64, message = "priceItemCode는 64자 이하여야 합니다.")
    private String priceItemCode;

    @Schema(example = "1000")
    @PositiveOrZero(message = "priceQty는 0 이상이어야 합니다.")
    private Long priceQty;

    @Schema(example = "starter_pack_1")
    @Size(max = 128, message = "storeSku는 128자 이하여야 합니다.")
    private String storeSku;

    @Schema(example = "UNLIMITED")
    @Size(max = 16, message = "purchaseLimitType은 16자 이하여야 합니다.")
    private String purchaseLimitType;

    @Schema(example = "0")
    @PositiveOrZero(message = "purchaseLimitCount는 0 이상이어야 합니다.")
    private Integer purchaseLimitCount;

    private JSONB buyCondition;

    private JSONB discount;

    private JSONB tags;

    @NotBlank(message = "createdBy는 필수입니다.")
    @Size(max = 64, message = "createdBy는 64자 이하여야 합니다.")
    private String createdBy;
}
