package com.qwerty.nexus.domain.game.user.entity;

import com.qwerty.nexus.domain.auth.Provider;
import lombok.Builder;
import lombok.Getter;
import org.jooq.generated.tables.records.GameUserRecord;

import java.time.OffsetDateTime;

@Getter
@Builder
public class GameUserEntity {
    private Integer userId;
    private Integer gameId;
    private String userLId;
    private String userLPw;
    private Provider provider;
    private String socialId;
    private String nickname;
    private String device;
    private OffsetDateTime blockStartDate;
    private OffsetDateTime blockEndDate;
    private String blockReason;
    private String isWithdrawal;
    private OffsetDateTime withdrawalDate;
    private String withdrawalReason;
    private OffsetDateTime createdAt;
    private String createdBy;
    private OffsetDateTime updatedAt;
    private String updatedBy;
    private String isDel;

    public static GameUserEntity from(GameUserRecord record){
        return GameUserEntity.builder()
                .userId(record.getUserId())
                .gameId(record.getGameId())
                .userLId(record.getUserLId())
                .userLPw(record.getUserLPw())
                .provider(Provider.valueOf(record.getProvider()))
                .socialId(record.getSocialId())
                .nickname(record.getNickname())
                .device(record.getDevice())
                .blockStartDate(record.getBlockStartDate())
                .blockEndDate(record.getBlockEndDate())
                .blockReason(record.getBlockReason())
                .isWithdrawal(record.getIsWithdrawal())
                .withdrawalDate(record.getWithdrawalDate())
                .withdrawalReason(record.getWithdrawalReason())
                .createdAt(record.getCreatedAt())
                .createdBy(record.getCreatedBy())
                .updatedAt(record.getUpdatedAt())
                .updatedBy(record.getUpdatedBy())
                .isDel(record.getIsDel())
                .build();
    }
}
