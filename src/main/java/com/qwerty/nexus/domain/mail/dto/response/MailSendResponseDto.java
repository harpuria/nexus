package com.qwerty.nexus.domain.mail.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class MailSendResponseDto {
    private Long templateId;
    private long dispatchedCount;
    private Set<Long> recipientIds;
}

