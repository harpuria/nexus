package com.qwerty.nexus.domain.game.coupon.command;

import com.qwerty.nexus.domain.game.coupon.dto.request.CouponCreateRequestDto;
import lombok.Builder;
import lombok.Getter;
import org.jooq.JSONB;

import java.time.OffsetDateTime;

@Getter
@Builder
public class CouponCreateCommand {
    private Integer gameId;
    private String name;
    private Long desc;
    private String code;
    private JSONB rewards;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private Long maxIssueCount;
    private Integer useLimitPerUser;
    private String createdBy;
    private String updatedBy;

    public static CouponCreateCommand from(CouponCreateRequestDto dto) {
        return CouponCreateCommand.builder()
                .gameId(dto.getGameId())
                .name(dto.getName())
                .desc(dto.getDesc())
                .code(dto.getCode())
                .rewards(dto.getRewards())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .maxIssueCount(dto.getMaxIssueCount())
                .useLimitPerUser(dto.getUseLimitPerUser())
                .createdBy(dto.getCreatedBy())
                .updatedBy(dto.getUpdatedBy())
                .build();
    }
}
