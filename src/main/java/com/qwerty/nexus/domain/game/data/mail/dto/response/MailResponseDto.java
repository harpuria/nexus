package com.qwerty.nexus.domain.game.data.mail.dto.response;

import com.qwerty.nexus.domain.game.data.mail.entity.MailEntity;
import com.qwerty.nexus.global.paging.dto.BaseResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jooq.JSONB;

import java.util.Objects;

@Getter
@SuperBuilder
public class MailResponseDto extends BaseResponseDto {
    private Integer mailId;
    private Integer gameId;
    private String title;
    private String content;
    private JSONB rewards;
    private String sendType;
    private Long expireAt;

    public static MailResponseDto from(MailEntity entity) {
        Objects.requireNonNull(entity, "entity must not be null");

        return MailResponseDto.builder()
                .mailId(entity.getMailId())
                .gameId(entity.getGameId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .rewards(entity.getRewards())
                .sendType(entity.getSendType())
                .expireAt(entity.getExpireAt())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .isDel(entity.getIsDel())
                .build();
    }
}
