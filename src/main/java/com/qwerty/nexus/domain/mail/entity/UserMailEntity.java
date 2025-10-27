package com.qwerty.nexus.domain.mail.entity;

import com.qwerty.nexus.domain.mail.UserMailStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

@Getter
@Builder(toBuilder = true)
@Accessors(fluent = true)
public class UserMailEntity {
    private final Long userMailId;
    private final Long templateId;
    private final Long userId;
    private final String title;
    private final String content;
    private final String rewardItem;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime readAt;
    private final OffsetDateTime receivedAt;
    private final UserMailStatus status;

    public boolean isUnread() {
        return status == UserMailStatus.UNREAD;
    }

    public boolean isRead() {
        return status == UserMailStatus.READ || status == UserMailStatus.RECEIVED;
    }

    public boolean isReceived() {
        return status == UserMailStatus.RECEIVED;
    }
}

