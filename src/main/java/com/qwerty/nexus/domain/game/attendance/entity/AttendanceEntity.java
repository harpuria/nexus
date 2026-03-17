package com.qwerty.nexus.domain.game.attendance.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
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

    private Integer userAttendanceId;
    private Long userId;
    private LocalDate lastAttendedDate;
    private Integer currentStreak;
    private Integer totalAttendCount;
    private Integer lastRewardedDayNo;
    private String todayReceivedYn;
    private String completedYn;
    private Integer resetCount;
    private Long lastGrantId;
    private String lastIdempotencyKey;
}
