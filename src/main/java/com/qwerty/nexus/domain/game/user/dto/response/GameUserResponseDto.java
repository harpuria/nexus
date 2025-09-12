package com.qwerty.nexus.domain.game.user.dto.response;

import com.qwerty.nexus.domain.game.user.entity.GameUserEntity;
import com.qwerty.nexus.global.extend.dto.BaseResponseDto;
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
    private String loginType;
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
                .loginType(entity.getLoginType())
                .device(entity.getDevice())
                .blockStartDate(entity.getBlockStartDate())
                .blockEndDate(entity.getBlockEndDate())
                .blockReason(entity.getBlockReason())
                .isWithdrawal(entity.getIsWithdrawal())
                .withdrawalDate(entity.getWithdrawalDate())
                .withdrawalReason(entity.getWithdrawalReason())
                .build();
    }
}
