package com.qwerty.nexus.domain.game.user.dto.response;

import com.qwerty.nexus.domain.auth.Provider;
import com.qwerty.nexus.domain.game.user.entity.GameUserEntity;
import com.qwerty.nexus.global.paging.dto.BaseResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

@Getter
@SuperBuilder
public class GameUserResponseDto extends BaseResponseDto {
    private Integer userId;
    private Integer gameId;
    private String userLId;
    private String userLPw;
    private String nickname;
    private Provider provider;
    private String device;
    private OffsetDateTime blockStartDate;
    private OffsetDateTime blockEndDate;
    private String blockReason;
    private String isWithdrawal;
    private OffsetDateTime withdrawalDate;
    private String withdrawalReason;

    public static GameUserResponseDto from(GameUserEntity entity){
        return GameUserResponseDto.builder()
                .userId(entity.getUserId())
                .gameId(entity.getGameId())
                .userLId(entity.getUserLId())
                .userLPw(entity.getUserLPw())
                .nickname(entity.getNickname())
                .provider(entity.getProvider())
                .device(entity.getDevice())
                .blockStartDate(entity.getBlockStartDate())
                .blockEndDate(entity.getBlockEndDate())
                .blockReason(entity.getBlockReason())
                .isWithdrawal(entity.getIsWithdrawal())
                .withdrawalDate(entity.getWithdrawalDate())
                .withdrawalReason(entity.getWithdrawalReason())
                .isDel(entity.getIsDel())
                .build();
    }
}
