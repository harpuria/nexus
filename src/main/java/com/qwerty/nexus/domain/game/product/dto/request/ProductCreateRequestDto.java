package com.qwerty.nexus.domain.game.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jooq.JSONB;

@Getter
@Setter
@NoArgsConstructor
public class ProductCreateRequestDto {
    @Schema(example = "1")
    @NotNull(message = "gameId는 필수입니다.")
    @Positive(message = "gameId는 1 이상이어야 합니다.")
    private Integer gameId;

    @Schema(example = "STARTER_PACK")
    @NotBlank(message = "productCode는 필수입니다.")
    @Size(max = 64, message = "productCode는 64자 이하여야 합니다.")
    private String productCode;

    @Schema(example = "상품1")
    @NotBlank(message = "상품명은 필수입니다.")
    @Size(max = 255, message = "상품명은 255자 이하여야 합니다.")
    private String name;

    @Schema(example = "단일상품 1 입니다.")
    private String desc;

    @Schema(example = "https://cdn.nexus.com/product/starter-pack.png")
    private String imageUrl;

    @Schema(example = "PACKAGE")
    @NotBlank(message = "productType은 필수입니다.")
    @Size(max = 32, message = "productType은 32자 이하여야 합니다.")
    private String productType;

    @Schema(example = "[{\"itemCode\":\"GEM\",\"qty\":1000}]")
    @NotNull(message = "보상 정보는 필수입니다.")
    private JSONB rewards;

    @NotBlank(message = "createdBy는 필수입니다.")
    @Size(max = 64, message = "createdBy는 64자 이하여야 합니다.")
    private String createdBy;

    @NotBlank(message = "updatedBy는 필수입니다.")
    @Size(max = 64, message = "updatedBy는 64자 이하여야 합니다.")
    private String updatedBy;
}
