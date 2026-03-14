package com.qwerty.nexus.domain.game.store.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
public class StoreProductUpdateRequestDto {
    @Schema(hidden = true)
    @JsonIgnore
    @Positive(message = "shopProductId는 1 이상이어야 합니다.")
    private Integer shopProductId;

    @Positive(message = "gameId는 1 이상이어야 합니다.")
    private Integer gameId;

    @Positive(message = "shopId는 1 이상이어야 합니다.")
    private Integer shopId;

    @Positive(message = "productId는 1 이상이어야 합니다.")
    private Integer productId;

    @PositiveOrZero(message = "sortOrder는 0 이상이어야 합니다.")
    private Integer sortOrder;

    @Pattern(regexp = "^[YN]$", message = "isVisible 값은 Y 또는 N 이어야 합니다.")
    private String isVisible;

    @Size(max = 16, message = "timeLimitType은 16자 이하여야 합니다.")
    private String timeLimitType;

    private LocalDateTime saleStartAt;

    private LocalDateTime saleEndAt;

    @Size(max = 16, message = "priceType은 16자 이하여야 합니다.")
    private String priceType;

    @Size(max = 64, message = "priceItemCode는 64자 이하여야 합니다.")
    private String priceItemCode;

    @PositiveOrZero(message = "priceAmount는 0 이상이어야 합니다.")
    private Long priceAmount;

    @Size(max = 128, message = "storeSku는 128자 이하여야 합니다.")
    private String storeSku;

    @Size(max = 16, message = "purchaseLimitType은 16자 이하여야 합니다.")
    private String purchaseLimitType;

    @PositiveOrZero(message = "purchaseLimitCount는 0 이상이어야 합니다.")
    private Integer purchaseLimitCount;

    private JSONB buyCondition;

    private JSONB discount;

    private JSONB tags;

    @Pattern(regexp = "^[YN]$", message = "isDel 값은 Y 또는 N 이어야 합니다.")
    private String isDel;

    @NotBlank(message = "updatedBy는 필수입니다.")
    @Size(max = 64, message = "updatedBy는 64자 이하여야 합니다.")
    private String updatedBy;
}
