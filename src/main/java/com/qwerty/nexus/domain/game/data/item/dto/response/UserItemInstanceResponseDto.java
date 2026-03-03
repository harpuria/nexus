package com.qwerty.nexus.domain.game.data.item.dto.response;

import com.qwerty.nexus.domain.game.data.item.entity.UserItemInstanceEntity;
import com.qwerty.nexus.domain.game.data.item.result.UserItemInstanceListResult;
import com.qwerty.nexus.global.dto.BaseResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jooq.JSONB;

import java.time.OffsetDateTime;

@Getter
@SuperBuilder
public class UserItemInstanceResponseDto extends BaseResponseDto {
    private Integer userItemId;
    private Integer userId;
    private Integer itemId;
    private JSONB stateJson;
    private OffsetDateTime acquiredAt;
    private String itemName;

    public static UserItemInstanceResponseDto from(UserItemInstanceEntity entity) {
        return UserItemInstanceResponseDto.builder()
                .userItemId(entity.getUserItemId())
                .userId(entity.getUserId())
                .itemId(entity.getItemId())
                .stateJson(entity.getStateJson())
                .acquiredAt(entity.getAcquiredAt())
                .build();
    }

    public static UserItemInstanceResponseDto from(UserItemInstanceListResult result) {
        return UserItemInstanceResponseDto.builder()
                .userItemId(result.getUserItemId())
                .itemName(result.getItemName())
                .stateJson(result.getStateJson())
                .build();
    }
}
