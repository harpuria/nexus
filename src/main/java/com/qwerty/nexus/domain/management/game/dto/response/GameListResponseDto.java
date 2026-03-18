package com.qwerty.nexus.domain.management.game.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record GameListResponseDto(
        boolean hasPrevious) {}
