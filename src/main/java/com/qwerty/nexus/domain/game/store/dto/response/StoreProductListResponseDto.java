package com.qwerty.nexus.domain.game.store.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record StoreProductListResponseDto(
        List<StoreProductResponseDto> storeProducts,
        int page,
        int size,
        long totalCount,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {
}
