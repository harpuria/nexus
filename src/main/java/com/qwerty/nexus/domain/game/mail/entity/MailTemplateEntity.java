package com.qwerty.nexus.domain.game.mail.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

@Getter
@Builder(toBuilder = true)
@Accessors(fluent = true)
public class MailTemplateEntity {
    private final Long templateId;
    private final String title;
    private final String content;
    private final String rewardItem;
    private final OffsetDateTime createdAt;
}

