package com.qwerty.nexus.domain.game.user.dto.request;

import com.qwerty.nexus.domain.game.user.command.GameUserWithdrawalCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
public class GameUserWithdrawalRequestDto {
    @Schema(example = "1")
    private Integer userId;

    @Schema(example = "Y")
    private String isWithdrawal;

    @Schema(example = "2025-08-11")
    private OffsetDateTime withdrawalDate;

    @Schema(example = "게임이 재미가 없어서")
    private String withdrawalReason;

    @Schema(example = "userTest")
    private String updatedBy;

    public GameUserWithdrawalCommand toCommand(){
        return GameUserWithdrawalCommand.builder()
                .userId(this.userId)
                .isWithdrawal(this.isWithdrawal)
                .withdrawalDate(this.withdrawalDate)
                .withdrawalReason(this.withdrawalReason)
                .updatedBy(this.updatedBy)
                .build();
    }
}
