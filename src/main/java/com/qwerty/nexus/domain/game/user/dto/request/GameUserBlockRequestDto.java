package com.qwerty.nexus.domain.game.user.dto.request;

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

    @Schema(example = "2025-08-10")
    private OffsetDateTime blockStartDate;

    @Schema(example = "2025-08-15")
    private OffsetDateTime blockEndDate;

    @Schema(example = "정지 사유")
    private String blockReason;

    @Schema(example = "userTest")
    private String updatedBy;

    @Schema(example = "7")
    private int blockDay; // blockEndDate 를 빈 값으로 둘 경우 해당 값을 기준으로 일수 계산 처리 (no database column)

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
