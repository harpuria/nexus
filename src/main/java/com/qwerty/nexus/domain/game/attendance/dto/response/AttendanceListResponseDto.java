package com.qwerty.nexus.domain.game.attendance.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record AttendanceListResponseDto(
        List<AttendanceResponseDto> attendances,
        int page,
        int size,
        long totalCount,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {}
