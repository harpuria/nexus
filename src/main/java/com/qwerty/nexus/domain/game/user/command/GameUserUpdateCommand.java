package com.qwerty.nexus.domain.game.user.command;

import com.qwerty.nexus.domain.game.user.dto.request.GameUserUpdateRequestDto;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
public class GameUserUpdateCommand {
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
    private String updatedBy;
    private String isDel;

    public static GameUserUpdateCommand from(GameUserUpdateRequestDto dto){
        return GameUserUpdateCommand.builder()
                .userId(dto.getUserId())
                .gameId(dto.getGameId())
                .userLId(dto.getUserLId())
                .userLPw(dto.getUserLPw())
                .nickname(dto.getNickname())
                .loginType(dto.getLoginType())
                .device(dto.getDevice())
                .blockStartDate(dto.getBlockStartDate())
                .blockEndDate(dto.getBlockEndDate())
                .blockReason(dto.getBlockReason())
                .isWithdrawal(dto.getIsWithdrawal())
                .withdrawalDate(dto.getWithdrawalDate())
                .withdrawalReason(dto.getWithdrawalReason())
                .updatedBy(dto.getUpdatedBy())
                .isDel(dto.getIsDel())
                .build();
    }
}
