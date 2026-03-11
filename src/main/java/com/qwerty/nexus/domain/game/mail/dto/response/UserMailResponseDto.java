package com.qwerty.nexus.domain.game.mail.dto.response;

import com.qwerty.nexus.domain.game.mail.entity.UserMailEntity;
import com.qwerty.nexus.domain.game.reward.dto.RewardDto;
import com.qwerty.nexus.global.dto.BaseResponseDto;
import com.qwerty.nexus.global.util.CommonUtil;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@SuperBuilder
public class UserMailResponseDto extends BaseResponseDto {
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
    private List<RewardDto> rewards;
    private OffsetDateTime expireAt;

    public static UserMailResponseDto from(UserMailEntity entity) {
        return UserMailResponseDto.builder()
                .userMailId(entity.getUserMailId())
                .gameId(entity.getGameId())
                .mailId(entity.getMailId())
                .userId(entity.getUserId())
                .isRead(entity.getIsRead())
                .readAt(entity.getReadAt())
                .isReceived(entity.getIsReceived())
                .receivedAt(entity.getReceivedAt())
                .title(entity.getTitle())
                .content(entity.getContent())
                .rewards(CommonUtil.jsonbToDto(entity.getRewards(), RewardDto.class))
                .expireAt(entity.getExpireAt())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .isDel(entity.getIsDel())
                .build();
    }
}
