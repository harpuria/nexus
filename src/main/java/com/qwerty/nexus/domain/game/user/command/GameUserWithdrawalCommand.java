package com.qwerty.nexus.domain.game.user.command;

import com.qwerty.nexus.domain.game.user.dto.request.GameUserWithdrawalRequestDto;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
public class GameUserWithdrawalCommand {
    private Integer userId;
    private String isWithdrawal;
    private OffsetDateTime withdrawalDate;
    private String withdrawalReason;
    private String updatedBy;

    public static GameUserWithdrawalCommand from (GameUserWithdrawalRequestDto dto){
        return GameUserWithdrawalCommand.builder()
                .userId(dto.getUserId())
                .isWithdrawal(dto.getIsWithdrawal())
                .withdrawalDate(dto.getWithdrawalDate())
                .withdrawalReason(dto.getWithdrawalReason())
                .updatedBy(dto.getUpdatedBy())
                .build();
    }
}
