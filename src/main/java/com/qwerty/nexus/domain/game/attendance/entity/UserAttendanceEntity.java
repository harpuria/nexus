package com.qwerty.nexus.domain.game.attendance.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Builder
public class UserAttendanceEntity {
    private Integer userAttendanceId;
    private Integer gameId;
    private Integer attendanceId;
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
    private OffsetDateTime createdAt;
    private String createdBy;
    private OffsetDateTime updatedAt;
    private String updatedBy;
    private String isDel;
}
