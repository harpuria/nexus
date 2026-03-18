package com.qwerty.nexus.domain.game.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Schema(example = "예시 설명입니다.")
    private String withdrawalReason;

    @Schema(example = "admin")
    private String updatedBy;

    // no parameter
    @JsonIgnore
    @Schema(hidden = true)
    private String isWithdrawal;

    @JsonIgnore
    @Schema(hidden = true)
    private OffsetDateTime withdrawalDate;
}
