package com.qwerty.nexus.domain.game.mail.dto.response;

import com.qwerty.nexus.domain.game.mail.UserMailStatus;
import com.qwerty.nexus.domain.game.mail.entity.UserMailEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
public class UserMailResponseDto {
    private Long userMailId;
    private Long templateId;
    private Long userId;
    private String title;
    private String content;
    private String rewardItem;
    private OffsetDateTime createdAt;
    private OffsetDateTime readAt;
    private OffsetDateTime receivedAt;
    private UserMailStatus status;

    public static UserMailResponseDto from(UserMailEntity entity) {
        return UserMailResponseDto.builder()
                .userMailId(entity.userMailId())
                .templateId(entity.templateId())
                .userId(entity.userId())
                .title(entity.title())
                .content(entity.content())
                .rewardItem(entity.rewardItem())
                .createdAt(entity.createdAt())
                .readAt(entity.readAt())
                .receivedAt(entity.receivedAt())
                .status(entity.status())
                .build();
    }
}

