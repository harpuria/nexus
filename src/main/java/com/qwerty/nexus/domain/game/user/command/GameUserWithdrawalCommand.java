package com.qwerty.nexus.domain.game.user.command;

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
}
