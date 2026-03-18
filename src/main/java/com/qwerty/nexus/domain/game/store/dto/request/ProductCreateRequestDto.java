package com.qwerty.nexus.domain.game.store.dto.request;

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
    @NotNull(message = "gameId는 필수입니다.")
    @Positive(message = "gameId는 1 이상이어야 합니다.")
    @Schema(example = "1")
    private Integer gameId;

    @NotBlank(message = "productCode는 필수입니다.")
    @Size(max = 64, message = "productCode는 64자 이하여야 합니다.")
    @Schema(example = "TEST_CODE_001")
    private String productCode;

    @NotBlank(message = "상품명은 필수입니다.")
    @Size(max = 255, message = "상품명은 255자 이하여야 합니다.")
    @Schema(example = "테스트 데이터")
    private String name;

    @Schema(example = "예시 설명입니다.")
    private String desc;

    @Schema(example = "sample")
    private String imageUrl;

    @NotBlank(message = "productType은 필수입니다.")
    @Size(max = 32, message = "productType은 32자 이하여야 합니다.")
    @Schema(example = "sample")
    private String productType;

    @NotNull(message = "보상 정보는 필수입니다.")
    @Schema(example = "[{\"itemId\":1,\"qty\":100}]")
    private JSONB rewards;

    @NotBlank(message = "createdBy는 필수입니다.")
    @Size(max = 64, message = "createdBy는 64자 이하여야 합니다.")
    @Schema(example = "admin")
    private String createdBy;

    @NotBlank(message = "updatedBy는 필수입니다.")
    @Size(max = 64, message = "updatedBy는 64자 이하여야 합니다.")
    @Schema(example = "admin")
    private String updatedBy;
}
