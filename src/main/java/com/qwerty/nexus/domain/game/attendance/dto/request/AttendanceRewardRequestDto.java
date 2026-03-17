package com.qwerty.nexus.domain.game.attendance.dto.request;

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
    private Integer dayNo;
    @NotNull @Positive
    private Integer rewardSeq;
    @NotBlank @Size(max = 32)
    private String rewardType;
    @NotNull @Positive
    private Integer itemId;
    @NotBlank @Size(max = 64)
    private String itemCode;
    @NotNull @Positive
    private Long rewardQty;
    @Size(max = 1)
    private String isBonus;
}
