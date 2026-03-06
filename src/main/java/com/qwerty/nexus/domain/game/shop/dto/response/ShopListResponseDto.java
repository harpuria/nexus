package com.qwerty.nexus.domain.game.shop.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ShopListResponseDto(
        List<ShopResponseDto> shops,
        int page,
        int size,
        long totalCount,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {}
