package com.qwerty.nexus.domain.game.mail.command;

import com.qwerty.nexus.domain.game.mail.dto.request.MailTemplateCreateRequestDto;

public record MailTemplateCreateCommand(
        String title,
        String content,
        String rewardItem
) {
    public static MailTemplateCreateCommand from(MailTemplateCreateRequestDto dto) {
        return new MailTemplateCreateCommand(
                dto.getTitle(),
                dto.getContent(),
                dto.getRewardItem()
        );
    }
}

