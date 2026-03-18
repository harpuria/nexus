package com.qwerty.nexus.domain.game.attendance.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AttendanceRewardRequestDto {
    @NotNull @Positive
    @Schema(example = "1")
    private Integer dayNo;
    @NotNull @Positive
    @Schema(example = "1")
    private Integer rewardSeq;
    @NotBlank @Size(max = 32)
    @Schema(example = "ITEM")
    private String rewardType;
    @NotNull @Positive
    @Schema(example = "1")
    private Integer itemId;
    @NotBlank @Size(max = 64)
    @Schema(example = "TEST_CODE_001")
    private String itemCode;
    @NotNull @Positive
    @Schema(example = "100")
    private Long rewardQty;
    @Size(max = 1)
    @Schema(example = "N")
    private String isBonus;
}
