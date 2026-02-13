package com.qwerty.nexus.domain.game.data.coupon.dto.response;

import com.qwerty.nexus.domain.game.data.coupon.entity.CouponEntity;
import com.qwerty.nexus.global.paging.dto.BaseResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jooq.JSONB;

import java.time.OffsetDateTime;

@Getter
@SuperBuilder
public class CouponResponseDto extends BaseResponseDto {
    private Integer couponId;
    private Integer gameId;
    private String name;
    private String desc;
    private String code;
    private JSONB rewards;
    private OffsetDateTime useStartDate;
    private OffsetDateTime useEndDate;
    private Long maxIssueCount;
    private Integer useLimitPerUser;

    public static CouponResponseDto from(CouponEntity entity) {
        return CouponResponseDto.builder()
                .couponId(entity.getCouponId())
                .gameId(entity.getGameId())
                .name(entity.getName())
                .desc(entity.getDesc())
                .code(entity.getCode())
                .rewards(entity.getRewards())
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
