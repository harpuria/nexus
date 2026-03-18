package com.qwerty.nexus.domain.game.item.dto.request;

import com.qwerty.nexus.domain.game.item.ItemType;
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

@Getter
@Setter
@NoArgsConstructor
public class ItemCreateRequestDto {
    @NotNull @Positive
    private Integer gameId;
    @NotBlank @Size(max = 64)
    private String itemCode;
    @NotBlank @Size(max = 255)
    private String name;
    @Size(max = 255)
    private String desc;
    private ItemType itemType;
    @NotBlank @Pattern(regexp = "^[YNyn]$")
    private String isStackable;
    @PositiveOrZero
    private Long defaultStack;
    @PositiveOrZero
    private Long maxStack;
    @Size(max = 32)
    private String rarity;
    @Size(max = 255)
    private String iconPath;
    @NotBlank @Size(max = 64)
    private String createdBy;
}
