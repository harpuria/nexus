package com.qwerty.nexus.domain.game.user.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Schema(example = "게임이 재미가 없어서")
    private String withdrawalReason;

    @Schema(example = "userTest")
    private String updatedBy;

    // no parameter
    @JsonIgnore
    private String isWithdrawal;

    @JsonIgnore
    private OffsetDateTime withdrawalDate;
}
