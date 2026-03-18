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
public class GameUserBlockRequestDto {
    @Schema(example = "1")
    private Integer userId;

    @Schema(example = "2026-03-18T09:00:00+09:00")
    private OffsetDateTime blockStartDate;

    @Schema(example = "예시 설명입니다.")
    private String blockReason;

    @Schema(example = "admin")
    private String updatedBy;

    // no database column
    @Schema(example = "1")
    private int blockDay; // 정지일수

    // no parameter
    @JsonIgnore
    @Schema(hidden = true)
    private OffsetDateTime blockEndDate;
}
