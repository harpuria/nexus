package com.qwerty.nexus.domain.game.user.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record GameUserListResponseDto(
        List<GameUserResponseDto> users,
        int page,
        int size,
        long totalCount,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {}
