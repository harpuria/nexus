package com.qwerty.nexus.domain.management.game.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GameListResponseDto {
    private final List<GameResponseDto> games;
    private final int page;
    private final int size;
    private final long totalCount;
    private final int totalPages;
    private final boolean hasNext;
    private final boolean hasPrevious;
}
