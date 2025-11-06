package com.qwerty.nexus.domain.management.game.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record GameListResponseDto(
        List<GameResponseDto> games,
        int page,
        int size,
        long totalCount,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious) {}
