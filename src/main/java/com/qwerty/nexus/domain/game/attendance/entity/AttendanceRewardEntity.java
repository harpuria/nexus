package com.qwerty.nexus.domain.game.attendance.entity;

import lombok.Builder;
import lombok.Getter;

import java.math.BigInteger;
import java.time.OffsetDateTime;

@Getter
@Builder
public class AttendanceRewardEntity {
    private Integer attendanceRewardId;
    private Integer attendanceId;
    private Integer dayNo;
    private Integer rewardSeq;
    private String rewardType;
    private Integer itemId;
    private String itemCode;
    private BigInteger rewardQty;
    private String isBonus;
    private OffsetDateTime createdAt;
    private String createdBy;
    private OffsetDateTime updatedAt;
    private String updatedBy;
    private String isDel;
}
