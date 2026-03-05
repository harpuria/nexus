package com.qwerty.nexus.domain.game.data.coupon.dto.response;

import com.qwerty.nexus.domain.game.data.coupon.entity.CouponEntity;
import com.qwerty.nexus.domain.reward.dto.RewardDto;
import com.qwerty.nexus.global.dto.BaseResponseDto;
import com.qwerty.nexus.global.util.CommonUtil;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@SuperBuilder
public class CouponResponseDto extends BaseResponseDto {
    private Integer couponId;
    private Integer gameId;
    private String name;
    private String desc;
    private String code;
    private List<RewardDto> rewards;
    private OffsetDateTime useStartDate;
    private OffsetDateTime useEndDate;
    private Long maxIssueCount;
    private Integer useLimitPerUser;

    public static CouponResponseDto from(CouponEntity entity) {
        Objects.requireNonNull(entity, "entity must not be null");

        return CouponResponseDto.builder()
                .couponId(entity.getCouponId())
                .gameId(entity.getGameId())
                .name(entity.getName())
                .desc(entity.getDesc())
                .code(entity.getCode())
                .rewards(CommonUtil.jsonbToDto(entity.getRewards(), RewardDto.class))
                .useStartDate(entity.getUseStartDate())
                .useEndDate(entity.getUseEndDate())
                .maxIssueCount(entity.getMaxIssueCount())
                .useLimitPerUser(entity.getUseLimitPerUser())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .isDel(entity.getIsDel())
                .build();
    }
}
