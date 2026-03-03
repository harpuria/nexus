package com.qwerty.nexus.domain.game.data.item.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ItemListResponseDto(
        List<ItemResponseDto> items,
        int page,
        int size,
        long totalCount,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {}
