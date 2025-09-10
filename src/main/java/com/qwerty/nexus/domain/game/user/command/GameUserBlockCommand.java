package com.qwerty.nexus.domain.game.user.command;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
public class GameUserBlockCommand {
    private Integer userId;
    private OffsetDateTime blockStartDate;
    private OffsetDateTime blockEndDate;
    private String blockReason;
    private int blockDay;
    private String updatedBy;
}
