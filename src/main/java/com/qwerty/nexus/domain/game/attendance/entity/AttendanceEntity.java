package com.qwerty.nexus.domain.game.attendance.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Builder
public class AttendanceEntity {
    private Integer attendanceId;
    private Integer gameId;
    private String attendanceCode;
    private String name;
    private String desc;
    private String attendanceType;
    private String periodType;
    private Integer maxDay;
    private String allowMissed;
    private String resetOnMiss;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String isActive;
    private OffsetDateTime createdAt;
    private String createdBy;
    private OffsetDateTime updatedAt;
    private String updatedBy;
    private String isDel;
}
