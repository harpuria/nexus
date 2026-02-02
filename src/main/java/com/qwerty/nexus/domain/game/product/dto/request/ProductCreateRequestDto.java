package com.qwerty.nexus.domain.game.product.dto.request;

import com.qwerty.nexus.domain.game.product.LimitType;
import com.qwerty.nexus.domain.game.product.PurchaseType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jooq.JSONB;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ProductCreateRequestDto {
    @Schema(example = "1")
    @NotNull(message = "gameId는 필수입니다.")
    @Positive(message = "gameId는 1 이상이어야 합니다.")
    private Integer gameId;

    @Schema(example = "CURRENCY")
    @NotNull(message = "purchaseType은 필수입니다.")
    private PurchaseType purchaseType;

    @Schema(example = "3")
    private Integer currencyId;

    @Schema(example = "상품1")
    @NotBlank(message = "상품명은 필수입니다.")
    @Size(max = 255, message = "상품명은 255자 이하여야 합니다.")
    private String name;

    @Schema(example = "단일상품 1 입니다.")
    @NotBlank(message = "상품 설명은 필수입니다.")
    private String desc;

    @Schema(example = "100")
    @NotNull(message = "가격은 필수입니다.")
    @Positive(message = "가격은 0보다 커야 합니다.")
    private BigDecimal price;

    @Schema(example = "[{\"currencyId\":1, \"amount\": 99999}]")
    @NotNull(message = "보상 정보는 필수입니다.")
    private JSONB rewards;

    @Schema(example = "NONE")
    @NotNull(message = "limitType은 필수입니다.")
    private LimitType limitType;
    private OffsetDateTime availableStart;
    private OffsetDateTime availableEnd;

    @NotBlank(message = "createdBy는 필수입니다.")
    @Size(max = 64, message = "createdBy는 64자 이하여야 합니다.")
    private String createdBy;

    @NotBlank(message = "updatedBy는 필수입니다.")
    @Size(max = 64, message = "updatedBy는 64자 이하여야 합니다.")
    private String updatedBy;

    @AssertTrue(message = "purchaseType이 CURRENCY인 경우 currencyId는 필수입니다.")
    public boolean isValidCurrencyIdForPurchaseType() {
        if (PurchaseType.CURRENCY != purchaseType) {
            return true;
        }
        return currencyId != null && currencyId > 0;
    }
}
