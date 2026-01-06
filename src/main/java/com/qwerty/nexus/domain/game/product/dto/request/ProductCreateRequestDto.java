package com.qwerty.nexus.domain.game.product.dto.request;

import com.qwerty.nexus.domain.game.product.LimitType;
import com.qwerty.nexus.domain.game.product.PurchaseType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jooq.JSONB;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductCreateRequestDto {
    @Schema(example = "1")
    private Integer gameId;

    @Schema(example = "CURRENCY")
    private PurchaseType purchaseType;

    @Schema(example = "3")
    private Integer currencyId;

    @Schema(example = "상품1")
    private String name;

    @Schema(example = "단일상품 1 입니다.")
    private String desc;

    @Schema(example = "100")
    private BigDecimal price;

    @Schema(example = "[{\"currencyId\":1, \"amount\": 99999}]")
    private JSONB rewards;

    @Schema(example = "NONE")
    private LimitType limitType;
    private OffsetDateTime availableStart;
    private OffsetDateTime availableEnd;
    private String createdBy;
    private String updatedBy;
}
