package com.qwerty.nexus.domain.game.data.currency.entity;

import com.qwerty.nexus.domain.game.data.currency.dto.response.CurrencyResponseDto;
import com.qwerty.nexus.domain.management.admin.dto.response.AdminResponseDto;
import lombok.Builder;

import java.util.List;

@Builder
public record CurrencyListResponseDto(
        List<CurrencyResponseDto> currencies,
        int page,
        int size,
        long totalCount,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious) {}