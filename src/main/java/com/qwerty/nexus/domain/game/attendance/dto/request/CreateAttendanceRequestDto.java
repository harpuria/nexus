package com.qwerty.nexus.domain.game.attendance.dto.request;

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
    private Integer gameId;
    @NotBlank @Size(max = 64)
    private String attendanceCode;
    @NotBlank @Size(max = 255)
    private String name;
    private String desc;
    @NotBlank @Size(max = 32)
    private String attendanceType;
    @NotBlank @Size(max = 32)
    private String periodType;
    @NotNull @Positive
    private Integer maxDay;
    @Size(max = 1)
    private String allowMissed;
    @Size(max = 1)
    private String resetOnMiss;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    @Size(max = 1)
    private String isActive;
    @NotBlank @Size(max = 64)
    private String createdBy;
}
