package com.qwerty.nexus.domain.game.store.dto.request;

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
    private Integer gameId;

    @Size(max = 64, message = "productCode는 64자 이하여야 합니다.")
    private String productCode;

    @Size(max = 255, message = "상품명은 255자 이하여야 합니다.")
    private String name;

    private String desc;
    private String imageUrl;

    @Size(max = 32, message = "productType은 32자 이하여야 합니다.")
    private String productType;

    private JSONB rewards;

    @Size(max = 64, message = "updatedBy는 64자 이하여야 합니다.")
    private String updatedBy;

    @Pattern(regexp = "^[YN]$", message = "isDel 값은 Y 또는 N 이어야 합니다.")
    private String isDel;

    // no parameter
    @JsonIgnore
    private Integer productId;
}
