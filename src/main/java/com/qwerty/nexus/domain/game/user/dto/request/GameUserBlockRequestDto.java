package com.qwerty.nexus.domain.game.user.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qwerty.nexus.domain.game.user.command.GameUserBlockCommand;
import com.qwerty.nexus.domain.game.user.command.GameUserUpdateCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
public class GameUserBlockRequestDto {
    @Schema(example = "1")
    private Integer userId;

    @Schema(example = "2025-09-15T00:00:00+09:00")
    private OffsetDateTime blockStartDate;

    @Schema(example = "정지 사유")
    private String blockReason;

    @Schema(example = "userTest")
    private String updatedBy;

    // no database column
    @Schema(example = "7")
    private int blockDay; // 정지일수

    // no parameter
    @JsonIgnore
    private OffsetDateTime blockEndDate;

    // Service 전달 파라미터로 쓸 Command 객체 변환
    public GameUserBlockCommand toCommand(){
        return GameUserBlockCommand.builder()
                .userId(this.userId)
                .blockStartDate(this.blockStartDate)
                .blockEndDate(this.blockEndDate)
                .blockReason(this.blockReason)
                .updatedBy(this.updatedBy)
                .blockDay(this.blockDay)
                .build();
    }

}
