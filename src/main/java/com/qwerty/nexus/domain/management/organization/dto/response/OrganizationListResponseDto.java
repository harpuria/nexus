package com.qwerty.nexus.domain.management.organization.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record OrganizationListResponseDto(
        List<OrganizationResponseDto> organizations,
        int page,
        int size,
        long totalCount,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious) {}

