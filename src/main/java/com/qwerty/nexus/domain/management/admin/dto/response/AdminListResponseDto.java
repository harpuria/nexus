package com.qwerty.nexus.domain.management.admin.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record AdminListResponseDto(
        List<AdminResponseDto> admins,
        int page,
        int size,
        long totalCount,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious) {}