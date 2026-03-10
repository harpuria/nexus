package com.qwerty.nexus.domain.game.mail.admin.dto.response;

import com.qwerty.nexus.domain.game.mail.admin.MailRecipientsType;
import com.qwerty.nexus.domain.game.mail.admin.MailSendType;
import com.qwerty.nexus.domain.game.mail.admin.entity.MailEntity;
import com.qwerty.nexus.domain.game.reward.dto.RewardDto;
import com.qwerty.nexus.global.dto.BaseResponseDto;
import com.qwerty.nexus.global.util.CommonUtil;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@SuperBuilder
public class MailResponseDto extends BaseResponseDto {
    private Integer mailId;
    private Integer gameId;
    private String title;
    private String content;
    private List<RewardDto> rewards;
    private MailSendType sendType;
    private MailRecipientsType recipientsType;
    private OffsetDateTime expireAt;

    public static MailResponseDto from(MailEntity entity) {
        Objects.requireNonNull(entity, "entity must not be null");

        return MailResponseDto.builder()
                .mailId(entity.getMailId())
                .gameId(entity.getGameId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .rewards(CommonUtil.jsonbToDto(entity.getRewards(), RewardDto.class))
                .sendType(entity.getSendType())
                .recipientsType(entity.getRecipientsType())
                .expireAt(entity.getExpireAt())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .isDel(entity.getIsDel())
                .build();
    }
}
