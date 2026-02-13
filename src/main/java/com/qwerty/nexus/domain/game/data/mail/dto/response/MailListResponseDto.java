package com.qwerty.nexus.domain.game.data.mail.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder(toBuilder = true)
public class MailListResponseDto {
    private List<MailResponseDto> mails;
    private int page;
    private int size;
    private long totalCount;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
}
