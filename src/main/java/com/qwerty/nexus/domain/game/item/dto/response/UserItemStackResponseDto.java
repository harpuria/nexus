package com.qwerty.nexus.domain.game.item.dto.response;

import com.qwerty.nexus.domain.game.item.entity.UserItemStackEntity;
import com.qwerty.nexus.domain.game.item.result.UserItemStackListResult;
import com.qwerty.nexus.global.dto.BaseResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class UserItemStackResponseDto extends BaseResponseDto {
    private Integer userItemStackId;
    private Integer userId;
    private Integer itemId;
    private Long amount;
    private String itemName;

    public static UserItemStackResponseDto from(UserItemStackEntity entity) {
        return UserItemStackResponseDto.builder()
                .userItemStackId(entity.getUserItemStackId())
                .userId(entity.getUserId())
                .itemId(entity.getItemId())
                .amount(entity.getAmount())
                .build();
    }

    public static UserItemStackResponseDto from(UserItemStackListResult result) {
        return UserItemStackResponseDto.builder()
                .userItemStackId(result.getUserItemStackId())
                .itemName(result.getItemName())
                .amount(result.getAmount())
                .build();
    }
}
