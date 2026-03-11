package com.qwerty.nexus.domain.game.mail.entity;

import lombok.Builder;
import lombok.Getter;
import org.jooq.JSONB;

import java.time.OffsetDateTime;

@Getter
@Builder
public class UserMailEntity {
    private Integer userMailId;
    private Integer gameId;
    private Integer mailId;
    private Integer userId;
    private String isRead;
    private OffsetDateTime readAt;
    private String isReceived;
    private OffsetDateTime receivedAt;
    private String title;
    private String content;
    private JSONB rewards;
    private OffsetDateTime expireAt;
    private OffsetDateTime createdAt;
    private String createdBy;
    private OffsetDateTime updatedAt;
    private String updatedBy;
    private String isDel;
}
