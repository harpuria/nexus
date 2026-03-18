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
public class CheckInAttendanceRequestDto {
    @NotNull @Positive
    @Schema(example = "1")
    private Integer gameId;
    @NotNull @Positive
    @Schema(example = "1")
    private Integer userId;
    @NotBlank @Size(max = 64)
    @Schema(example = "admin")
    private String updatedBy;
    @Size(max = 128)
    @Schema(example = "sample")
    private String idempotencyKey;
}
