package com.qwerty.nexus.domain.game.store.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jooq.JSONB;

@Getter
@Setter
@NoArgsConstructor
public class ProductUpdateRequestDto {
    @Schema(example = "1")
    private Integer gameId;

    @Size(max = 64, message = "productCode는 64자 이하여야 합니다.")
    @Schema(example = "TEST_CODE_001")
    private String productCode;

    @Size(max = 255, message = "상품명은 255자 이하여야 합니다.")
    @Schema(example = "테스트 데이터")
    private String name;

    @Schema(example = "예시 설명입니다.")
    private String desc;
    @Schema(example = "sample")
    private String imageUrl;

    @Size(max = 32, message = "productType은 32자 이하여야 합니다.")
    @Schema(example = "sample")
    private String productType;

    @Schema(example = "[{\"itemId\":1,\"qty\":100}]")
    private JSONB rewards;

    @Size(max = 64, message = "updatedBy는 64자 이하여야 합니다.")
    @Schema(example = "admin")
    private String updatedBy;

    @Pattern(regexp = "^[YN]$", message = "isDel 값은 Y 또는 N 이어야 합니다.")
    @Schema(example = "N")
    private String isDel;

    // no parameter
    @JsonIgnore
    @Schema(hidden = true)
    private Integer productId;
}
