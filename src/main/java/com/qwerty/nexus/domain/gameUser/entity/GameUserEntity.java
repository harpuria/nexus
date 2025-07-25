package com.qwerty.nexus.domain.gameUser.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
public class GameUserEntity {
    private Integer userId;
    private Integer gameId;
    private String userLId;
    private String userLPw;
    private String nickname;
    private String loginType;
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
}
