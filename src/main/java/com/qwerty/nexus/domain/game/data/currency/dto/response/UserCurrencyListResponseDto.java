package com.qwerty.nexus.domain.game.data.currency.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record UserCurrencyListResponseDto(
        List<UserCurrencyResponseDto> userCurrencies,
        int page,
        int size,
        long totalCount,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious) {
}
