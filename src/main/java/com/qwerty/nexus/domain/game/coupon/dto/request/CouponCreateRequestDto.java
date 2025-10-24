package com.qwerty.nexus.domain.game.coupon.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jooq.JSONB;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CouponCreateRequestDto {
    @Schema(example = "1")
    private Integer gameId;

    @Schema(example = "신규 유저 웰컴 쿠폰")
    private String name;

    @Schema(example = "1001")
    private Long desc;

    @Schema(example = "WELCOME100")
    private String code;

    @Schema(example = "[{\"currencyId\":1, \"amount\": 1000}]")
    private JSONB rewards;

    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private Long maxIssueCount;
    private Integer useLimitPerUser;
    private String createdBy;
    private String updatedBy;
}
