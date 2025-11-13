package com.qwerty.nexus.domain.game.product.dto.response;

import java.util.List;

import lombok.Builder;

@Builder
public record ProductListResponseDto(
        List<ProductResponseDto> products,
        int page,
        int size,
        long totalCount,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {}
