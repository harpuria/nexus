package com.qwerty.nexus.domain.game.coupon.command;

import com.qwerty.nexus.domain.game.coupon.dto.request.CouponUpdateRequestDto;
import lombok.Builder;
import lombok.Getter;
import org.jooq.JSONB;

import java.time.OffsetDateTime;

@Getter
@Builder
public class CouponUpdateCommand {
    private Integer couponId;
    private Integer gameId;
    private String name;
    private Long desc;
    private String code;
    private JSONB rewards;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private Long maxIssueCount;
    private Integer useLimitPerUser;
    private String updatedBy;
    private String isDel;

    public static CouponUpdateCommand from(CouponUpdateRequestDto dto) {
        return CouponUpdateCommand.builder()
                .couponId(dto.getCouponId())
                .gameId(dto.getGameId())
                .name(dto.getName())
                .desc(dto.getDesc())
                .code(dto.getCode())
                .rewards(dto.getRewards())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .maxIssueCount(dto.getMaxIssueCount())
                .useLimitPerUser(dto.getUseLimitPerUser())
                .updatedBy(dto.getUpdatedBy())
                .isDel(dto.getIsDel())
                .build();
    }
}
