package com.qwerty.nexus.domain.game.store.shop.dto.response;

import com.qwerty.nexus.domain.game.store.shop.entity.ShopEntity;
import com.qwerty.nexus.global.dto.BaseResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jooq.JSONB;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class ShopResponseDto extends BaseResponseDto {
    private Integer shopId;
    private Integer gameId;
    private String shopCode;
    private String name;
    private String desc;
    private String shopType;
    private String timeLimitType;
    private LocalDateTime openAt;
    private LocalDateTime closeAt;
    private JSONB openCondition;
    private Integer sortOrder;
    private String isVisible;

    public static ShopResponseDto from(ShopEntity entity) {
        return ShopResponseDto.builder()
                .shopId(entity.getShopId())
                .gameId(entity.getGameId())
                .shopCode(entity.getShopCode())
                .name(entity.getName())
                .desc(entity.getDesc())
                .shopType(entity.getShopType())
                .timeLimitType(entity.getTimeLimitType())
                .openAt(entity.getOpenAt())
                .closeAt(entity.getCloseAt())
                .openCondition(entity.getOpenCondition())
                .sortOrder(entity.getSortOrder())
                .isVisible(entity.getIsVisible())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .isDel(entity.getIsDel())
                .build();
    }
}
