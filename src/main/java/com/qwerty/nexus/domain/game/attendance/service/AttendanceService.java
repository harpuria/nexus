package com.qwerty.nexus.domain.game.attendance.service;

import com.qwerty.nexus.domain.game.attendance.dto.request.*;
import com.qwerty.nexus.domain.game.attendance.dto.response.AttendanceListResponseDto;
import com.qwerty.nexus.domain.game.attendance.dto.response.AttendanceResponseDto;
import com.qwerty.nexus.domain.game.attendance.dto.response.AttendanceRewardResponseDto;
import com.qwerty.nexus.domain.game.attendance.entity.AttendanceEntity;
import com.qwerty.nexus.domain.game.attendance.entity.AttendanceRewardEntity;
import com.qwerty.nexus.domain.game.attendance.entity.UserAttendanceEntity;
import com.qwerty.nexus.domain.game.attendance.repository.AttendanceRepository;
import com.qwerty.nexus.domain.game.reward.SourceType;
import com.qwerty.nexus.domain.game.reward.dto.GrantDto;
import com.qwerty.nexus.domain.game.reward.dto.RewardDto;
import com.qwerty.nexus.domain.game.reward.service.RewardService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.paging.PagingEntity;
import com.qwerty.nexus.global.paging.PagingRequestDto;
import com.qwerty.nexus.global.paging.PagingUtil;
import com.qwerty.nexus.global.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final RewardService rewardService;

    @Transactional
    public Result<Void> createAttendance(CreateAttendanceRequestDto requestDto) {
        Integer attendanceId = attendanceRepository.insertAttendance(AttendanceEntity.builder()
                .gameId(requestDto.getGameId())
                .attendanceCode(requestDto.getAttendanceCode())
                .name(requestDto.getName())
                .desc(requestDto.getDesc())
                .attendanceType(requestDto.getAttendanceType())
                .periodType(requestDto.getPeriodType())
                .maxDay(requestDto.getMaxDay())
                .allowMissed(defaultYn(requestDto.getAllowMissed(), "Y"))
                .resetOnMiss(defaultYn(requestDto.getResetOnMiss(), "N"))
                .startAt(requestDto.getStartAt())
                .endAt(requestDto.getEndAt())
                .isActive(defaultYn(requestDto.getIsActive(), "N"))
                .createdBy(requestDto.getCreatedBy())
                .updatedBy(requestDto.getCreatedBy())
                .isDel("N")
                .build());

        if (attendanceId == null || attendanceId <= 0) {
            return Result.Failure.of("출석 이벤트 생성에 실패했습니다.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.CREATED);
    }

    @Transactional
    public Result<Void> updateAttendance(UpdateAttendanceRequestDto requestDto) {
        if (attendanceRepository.findByAttendanceId(requestDto.getAttendanceId()).isEmpty()) {
            return Result.Failure.of("출석 이벤트 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        int updatedCount = attendanceRepository.updateAttendance(AttendanceEntity.builder()
                .attendanceId(requestDto.getAttendanceId())
                .name(requestDto.getName())
                .desc(requestDto.getDesc())
                .attendanceType(requestDto.getAttendanceType())
                .periodType(requestDto.getPeriodType())
                .maxDay(requestDto.getMaxDay())
                .allowMissed(requestDto.getAllowMissed())
                .resetOnMiss(requestDto.getResetOnMiss())
                .startAt(requestDto.getStartAt())
                .endAt(requestDto.getEndAt())
                .isActive(requestDto.getIsActive())
                .isDel(requestDto.getIsDel())
                .updatedBy(requestDto.getUpdatedBy())
                .build());

        if (updatedCount <= 0) {
            return Result.Failure.of("출석 이벤트 수정에 실패했습니다.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.UPDATED);
    }

    @Transactional(readOnly = true)
    public Result<AttendanceListResponseDto> listAttendance(PagingRequestDto pagingRequestDto, Integer gameId, boolean activeOnly) {
        PagingEntity pagingEntity = PagingUtil.getPagingEntity(pagingRequestDto);
        if (pagingEntity == null) {
            return Result.Failure.of("페이징 정보가 올바르지 않습니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        List<AttendanceResponseDto> attendances = attendanceRepository.findAllByGameId(pagingEntity, gameId, activeOnly)
                .stream()
                .map(this::toAttendanceResponseDto)
                .toList();

        long totalCount = attendanceRepository.countByGameId(gameId, activeOnly);
        int totalPages = pagingEntity.getSize() == 0 ? 0 : (int) Math.ceil((double) totalCount / pagingEntity.getSize());

        AttendanceListResponseDto responseDto = AttendanceListResponseDto.builder()
                .attendances(attendances)
                .page(pagingEntity.getPage())
                .size(pagingEntity.getSize())
                .totalCount(totalCount)
                .totalPages(totalPages)
                .hasNext(pagingEntity.getPage() + 1 < totalPages)
                .hasPrevious(pagingEntity.getPage() > 0 && totalPages > 0)
                .build();

        return Result.Success.of(responseDto, ApiConstants.Messages.Success.RETRIEVED);
    }

    @Transactional(readOnly = true)
    public Result<AttendanceResponseDto> getAttendance(Integer attendanceId, Integer gameId) {
        Optional<AttendanceEntity> attendanceOptional = attendanceRepository.findByAttendanceIdAndGameId(attendanceId, gameId);
        if (attendanceOptional.isEmpty()) {
            return Result.Failure.of("출석 이벤트 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        return Result.Success.of(toAttendanceResponseDto(attendanceOptional.get()), ApiConstants.Messages.Success.RETRIEVED);
    }

    @Transactional
    public Result<Void> updateAttendanceRewards(Integer attendanceId, UpdateAttendanceRewardsRequestDto requestDto) {
        if (attendanceRepository.findByAttendanceId(attendanceId).isEmpty()) {
            return Result.Failure.of("출석 이벤트 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        attendanceRepository.deleteAttendanceRewards(AttendanceRewardEntity.builder()
                .attendanceId(attendanceId)
                .isDel("Y")
                .updatedBy(requestDto.getUpdatedBy())
                .build());

        for (AttendanceRewardRequestDto reward : requestDto.getRewards()) {
            Integer attendanceRewardId = attendanceRepository.insertAttendanceReward(AttendanceRewardEntity.builder()
                    .attendanceId(attendanceId)
                    .dayNo(reward.getDayNo())
                    .rewardSeq(reward.getRewardSeq())
                    .rewardType(reward.getRewardType())
                    .itemId(reward.getItemId())
                    .itemCode(reward.getItemCode())
                    .rewardQty(BigInteger.valueOf(reward.getRewardQty()))
                    .isBonus(defaultYn(reward.getIsBonus(), "N"))
                    .createdBy(requestDto.getUpdatedBy())
                    .updatedBy(requestDto.getUpdatedBy())
                    .isDel("N")
                    .build());

            if (attendanceRewardId == null || attendanceRewardId <= 0) {
                return Result.Failure.of("출석 보상 수정에 실패했습니다.", ErrorCode.INTERNAL_ERROR.getCode());
            }
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.UPDATED);
    }

    @Transactional
    public Result<Void> activateAttendance(Integer attendanceId, UpdateAttendanceStatusRequestDto requestDto) {
        return updateAttendanceStatus(attendanceId, "Y", requestDto.getUpdatedBy());
    }

    @Transactional
    public Result<Void> deactivateAttendance(Integer attendanceId, UpdateAttendanceStatusRequestDto requestDto) {
        return updateAttendanceStatus(attendanceId, "N", requestDto.getUpdatedBy());
    }

    @Transactional
    public Result<Void> checkInAttendance(Integer attendanceId, CheckInAttendanceRequestDto requestDto) {
        Optional<AttendanceEntity> attendanceOptional = attendanceRepository.findByAttendanceIdAndGameId(attendanceId, requestDto.getGameId());
        if (attendanceOptional.isEmpty()) {
            return Result.Failure.of("출석 이벤트 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        AttendanceEntity attendance = attendanceOptional.get();
        if (!"Y".equalsIgnoreCase(attendance.getIsActive())) {
            return Result.Failure.of("비활성화된 출석 이벤트입니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        LocalDateTime now = LocalDateTime.now();
        if (attendance.getStartAt() != null && now.isBefore(attendance.getStartAt())) {
            return Result.Failure.of("아직 시작되지 않은 출석 이벤트입니다.", ErrorCode.INVALID_REQUEST.getCode());
        }
        if (attendance.getEndAt() != null && now.isAfter(attendance.getEndAt())) {
            return Result.Failure.of("종료된 출석 이벤트입니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        Optional<UserAttendanceEntity> userAttendanceOptional = attendanceRepository.findUserAttendanceByGameIdAndAttendanceIdAndUserId(
                requestDto.getGameId(), attendanceId, requestDto.getUserId().longValue());

        LocalDate today = LocalDate.now();
        UserAttendanceEntity userAttendance = userAttendanceOptional.orElseGet(() -> UserAttendanceEntity.builder()
                .gameId(requestDto.getGameId())
                .attendanceId(attendanceId)
                .userId(requestDto.getUserId().longValue())
                .currentStreak(0)
                .totalAttendCount(0)
                .lastRewardedDayNo(0)
                .todayReceivedYn("N")
                .completedYn("N")
                .resetCount(0)
                .createdBy(requestDto.getUpdatedBy())
                .updatedBy(requestDto.getUpdatedBy())
                .isDel("N")
                .build());

        if (today.equals(userAttendance.getLastAttendedDate())) {
            return Result.Failure.of("오늘은 이미 출석 완료되었습니다.", ErrorCode.CONFLICT.getCode());
        }

        int currentStreak = userAttendance.getCurrentStreak() == null ? 0 : userAttendance.getCurrentStreak();
        int totalAttendCount = userAttendance.getTotalAttendCount() == null ? 0 : userAttendance.getTotalAttendCount();
        int dayNo;

        if ("STREAK".equalsIgnoreCase(attendance.getAttendanceType())) {
            if (userAttendance.getLastAttendedDate() != null && userAttendance.getLastAttendedDate().plusDays(1).equals(today)) {
                currentStreak += 1;
            } else {
                currentStreak = 1;
            }
            dayNo = currentStreak;
        } else {
            totalAttendCount += 1;
            dayNo = totalAttendCount;
        }

        if (dayNo > attendance.getMaxDay()) {
            return Result.Failure.of("이미 모든 출석 보상을 수령했습니다.", ErrorCode.CONFLICT.getCode());
        }

        List<AttendanceRewardEntity> rewards = attendanceRepository.findAllByAttendanceIdAndDayNo(attendanceId, dayNo);
        if (rewards.isEmpty()) {
            return Result.Failure.of("출석 보상 정보가 존재하지 않습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        boolean grantResult = rewardService.grant(GrantDto.builder()
                .gameId(requestDto.getGameId())
                .userId(requestDto.getUserId())
                .sourceType(SourceType.ATTENDANCE)
                .sourceId(String.format("%s:%s", attendanceId, dayNo))
                .rewards(rewards.stream().map(reward -> new RewardDto(reward.getItemId(), reward.getItemCode(), reward.getRewardQty().longValue())).toList())
                .build());

        if (!grantResult) {
            return Result.Failure.of("출석 보상 지급 처리 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        UserAttendanceEntity updateUserAttendance = UserAttendanceEntity.builder()
                .userAttendanceId(userAttendance.getUserAttendanceId())
                .gameId(requestDto.getGameId())
                .attendanceId(attendanceId)
                .userId(requestDto.getUserId().longValue())
                .lastAttendedDate(today)
                .currentStreak("STREAK".equalsIgnoreCase(attendance.getAttendanceType()) ? dayNo : currentStreak)
                .totalAttendCount(totalAttendCount + ("STREAK".equalsIgnoreCase(attendance.getAttendanceType()) ? 1 : 0))
                .lastRewardedDayNo(dayNo)
                .todayReceivedYn("Y")
                .completedYn(dayNo >= attendance.getMaxDay() ? "Y" : "N")
                .lastIdempotencyKey(requestDto.getIdempotencyKey())
                .updatedBy(requestDto.getUpdatedBy())
                .isDel("N")
                .build();

        int affected;
        if (userAttendance.getUserAttendanceId() == null) {
            Integer userAttendanceId = attendanceRepository.insertUserAttendance(updateUserAttendance);
            affected = userAttendanceId == null ? 0 : 1;
        } else {
            affected = attendanceRepository.updateUserAttendance(updateUserAttendance);
        }

        if (affected <= 0) {
            return Result.Failure.of("출석 처리 결과 저장에 실패했습니다.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.PROCESSED);
    }

    private Result<Void> updateAttendanceStatus(Integer attendanceId, String isActive, String updatedBy) {
        if (attendanceRepository.findByAttendanceId(attendanceId).isEmpty()) {
            return Result.Failure.of("출석 이벤트 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        int updatedCount = attendanceRepository.updateAttendance(AttendanceEntity.builder()
                .attendanceId(attendanceId)
                .isActive(isActive)
                .updatedBy(updatedBy)
                .build());

        if (updatedCount <= 0) {
            return Result.Failure.of("출석 이벤트 상태 수정에 실패했습니다.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.UPDATED);
    }

    private AttendanceResponseDto toAttendanceResponseDto(AttendanceEntity attendanceEntity) {
        List<AttendanceRewardResponseDto> rewards = attendanceRepository.findAllByAttendanceId(attendanceEntity.getAttendanceId())
                .stream()
                .map(AttendanceRewardResponseDto::from)
                .toList();

        return AttendanceResponseDto.from(attendanceEntity, rewards);
    }

    private String defaultYn(String value, String defaultValue) {
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
