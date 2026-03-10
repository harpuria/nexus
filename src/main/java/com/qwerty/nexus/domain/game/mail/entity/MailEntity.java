package com.qwerty.nexus.domain.game.mail.entity;

import com.qwerty.nexus.domain.game.mail.MailRecipientsType;
import com.qwerty.nexus.domain.game.mail.MailSendType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.jooq.JSONB;

import java.time.OffsetDateTime;

@Getter
@ToString
@Builder
public class MailEntity {
    private Integer mailId;
    private Integer gameId;
    private String title;
    private String content;
    private JSONB rewards;
    private MailSendType sendType;
    private MailRecipientsType recipientsType;
    private OffsetDateTime expireAt;
    private OffsetDateTime createdAt;
    private String createdBy;
    private OffsetDateTime updatedAt;
    private String updatedBy;
    private String isDel;
}
