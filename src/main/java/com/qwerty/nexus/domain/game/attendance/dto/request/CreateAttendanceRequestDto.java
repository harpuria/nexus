package com.qwerty.nexus.domain.game.attendance.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CreateAttendanceRequestDto {
    @NotNull @Positive
    @Schema(example = "1")
    private Integer gameId;
    @NotBlank @Size(max = 64)
    @Schema(example = "TEST_CODE_001")
    private String attendanceCode;
    @NotBlank @Size(max = 255)
    @Schema(example = "테스트 데이터")
    private String name;
    @Schema(example = "예시 설명입니다.")
    private String desc;
    @NotBlank @Size(max = 32)
    @Schema(example = "DAILY")
    private String attendanceType;
    @NotBlank @Size(max = 32)
    @Schema(example = "DAILY")
    private String periodType;
    @NotNull @Positive
    @Schema(example = "100")
    private Integer maxDay;
    @Size(max = 1)
    @Schema(example = "sample")
    private String allowMissed;
    @Size(max = 1)
    @Schema(example = "sample")
    private String resetOnMiss;
    @Schema(example = "2026-03-18T09:00:00")
    private LocalDateTime startAt;
    @Schema(example = "2026-03-18T09:00:00")
    private LocalDateTime endAt;
    @Size(max = 1)
    @Schema(example = "N")
    private String isActive;
    @NotBlank @Size(max = 64)
    @Schema(example = "admin")
    private String createdBy;
}
