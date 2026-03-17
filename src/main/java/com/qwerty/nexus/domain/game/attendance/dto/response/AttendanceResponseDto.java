package com.qwerty.nexus.domain.game.attendance.dto.response;

import com.qwerty.nexus.domain.game.attendance.entity.AttendanceEntity;
import com.qwerty.nexus.global.dto.BaseResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@SuperBuilder
public class AttendanceResponseDto extends BaseResponseDto {
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
    private List<AttendanceRewardResponseDto> rewards;

    public static AttendanceResponseDto from(AttendanceEntity entity, List<AttendanceRewardResponseDto> rewards) {
        return AttendanceResponseDto.builder()
                .attendanceId(entity.getAttendanceId())
                .gameId(entity.getGameId())
                .attendanceCode(entity.getAttendanceCode())
                .name(entity.getName())
                .desc(entity.getDesc())
                .attendanceType(entity.getAttendanceType())
                .periodType(entity.getPeriodType())
                .maxDay(entity.getMaxDay())
                .allowMissed(entity.getAllowMissed())
                .resetOnMiss(entity.getResetOnMiss())
                .startAt(entity.getStartAt())
                .endAt(entity.getEndAt())
                .isActive(entity.getIsActive())
                .rewards(rewards)
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .isDel(entity.getIsDel())
                .build();
    }
}
