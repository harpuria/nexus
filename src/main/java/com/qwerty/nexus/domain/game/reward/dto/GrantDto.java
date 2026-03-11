package com.qwerty.nexus.domain.game.reward.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GrantDto {
    Integer gameId;             // 게임 ID
    Integer userId;             // 유저 ID
    List<RewardDto> rewards;    // 보상 목록
    String sourceType;          // 보상 출처 타입 (ex: COUPON, SHOP, MAIL 등)
    String sourceId;            // 출처 식별자 (ex: mailId, couponCode 등)
}