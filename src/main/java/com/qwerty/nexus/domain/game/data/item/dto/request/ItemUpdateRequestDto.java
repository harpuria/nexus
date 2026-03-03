package com.qwerty.nexus.domain.game.data.item.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jooq.JSONB;

@Getter
@Setter
@NoArgsConstructor
public class ItemUpdateRequestDto {
    private Integer itemId;
    @Size(max = 255)
    private String name;
    @Size(max = 255)
    private String desc;
    @Size(max = 32)
    private String itemType;
    @Pattern(regexp = "^[YNyn]$")
    private String isStackable;
    @PositiveOrZero
    private Long maxStack;
    @Size(max = 32)
    private String rarity;
    @Size(max = 255)
    private String iconPath;
    private JSONB metaJson;
    @Size(max = 64)
    private String updatedBy;
    @Pattern(regexp = "^[YNyn]$")
    private String isDel;
}
