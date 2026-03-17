package com.qwerty.nexus.domain.game.attendance.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UpdateAttendanceRequestDto {
    private Integer attendanceId;
    private String name;
    private String desc;
    private String attendanceType;
    private String periodType;
    private Integer maxDay;
    @Size(max = 1)
    private String allowMissed;
    @Size(max = 1)
    private String resetOnMiss;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    @Size(max = 1)
    private String isActive;
    @Size(max = 1)
    private String isDel;
    @NotBlank @Size(max = 64)
    private String updatedBy;
}
