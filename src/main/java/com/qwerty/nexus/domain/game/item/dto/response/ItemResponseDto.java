package com.qwerty.nexus.domain.game.item.dto.response;

import com.qwerty.nexus.domain.game.item.entity.ItemEntity;
import com.qwerty.nexus.global.dto.BaseResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jooq.JSONB;

@Getter
@SuperBuilder
public class ItemResponseDto extends BaseResponseDto {
    private Integer itemId;
    private Integer gameId;
    private String itemCode;
    private String name;
    private String desc;
    private String itemType;
    private String isStackable;
    private Long maxStack;
    private String rarity;
    private String iconPath;
    private JSONB metaJson;

    public static ItemResponseDto from(ItemEntity entity) {
        return ItemResponseDto.builder()
                .itemId(entity.getItemId())
                .gameId(entity.getGameId())
                .itemCode(entity.getItemCode())
                .name(entity.getName())
                .desc(entity.getDesc())
                .itemType(entity.getItemType())
                .isStackable(entity.getIsStackable())
                .maxStack(entity.getMaxStack())
                .rarity(entity.getRarity())
                .iconPath(entity.getIconPath())
                .metaJson(entity.getMetaJson())
                .build();
    }
}
