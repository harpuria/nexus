package com.qwerty.nexus.domain.game.item.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import com.qwerty.nexus.domain.game.item.ItemType;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemUpdateRequestDto {
    @Schema(example = "1")
    private Integer itemId;
    @Size(max = 255)
    @Schema(example = "테스트 데이터")
    private String name;
    @Size(max = 255)
    @Schema(example = "예시 설명입니다.")
    private String desc;
    @Schema(example = "CURRENCY")
    private ItemType itemType;
    @Pattern(regexp = "^[YNyn]$")
    @Schema(example = "N")
    private String isStackable;
    @PositiveOrZero
    @Schema(example = "1")
    private Long defaultStack;
    @PositiveOrZero
    @Schema(example = "100")
    private Long maxStack;
    @Size(max = 32)
    @Schema(example = "LEGENDARY")
    private String rarity;
    @Size(max = 255)
    @Schema(example = "/images/sample.png")
    private String iconPath;
    @Size(max = 64)
    @Schema(example = "admin")
    private String updatedBy;
    @Pattern(regexp = "^[YNyn]$")
    @Schema(example = "N")
    private String isDel;
}
