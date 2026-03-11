package com.qwerty.nexus.domain.game.mail.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record UserMailListResponseDto(
        List<UserMailResponseDto> userMails,
        int page,
        int size,
        long totalCount,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {}
