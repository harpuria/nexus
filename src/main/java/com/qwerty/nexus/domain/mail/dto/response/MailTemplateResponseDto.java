package com.qwerty.nexus.domain.mail.dto.response;

import com.qwerty.nexus.domain.mail.entity.MailTemplateEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
public class MailTemplateResponseDto {
    private Long templateId;
    private String title;
    private String content;
    private String rewardItem;
    private OffsetDateTime createdAt;

    public static MailTemplateResponseDto from(MailTemplateEntity entity) {
        return MailTemplateResponseDto.builder()
                .templateId(entity.templateId())
                .title(entity.title())
                .content(entity.content())
                .rewardItem(entity.rewardItem())
                .createdAt(entity.createdAt())
                .build();
    }
}

