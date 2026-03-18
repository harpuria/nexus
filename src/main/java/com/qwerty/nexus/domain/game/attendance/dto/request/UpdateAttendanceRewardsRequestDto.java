package com.qwerty.nexus.domain.game.attendance.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UpdateAttendanceRewardsRequestDto {
    @NotEmpty
    @Schema(example = "[]")
    private List<@Valid AttendanceRewardRequestDto> rewards;
    @NotBlank @Size(max = 64)
    @Schema(example = "admin")
    private String updatedBy;
}
