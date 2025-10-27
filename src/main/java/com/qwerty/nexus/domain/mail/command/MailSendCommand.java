package com.qwerty.nexus.domain.mail.command;

import com.qwerty.nexus.domain.mail.MailTargetType;
import com.qwerty.nexus.domain.mail.dto.request.MailSendRequestDto;

import java.util.List;

public record MailSendCommand(
        Long templateId,
        MailTargetType targetType,
        List<Long> recipientIds
) {
    public static MailSendCommand from(MailSendRequestDto dto) {
        return new MailSendCommand(
                dto.getTemplateId(),
                dto.getTargetType(),
                dto.getRecipientIds()
        );
    }
}

