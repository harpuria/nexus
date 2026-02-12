package com.qwerty.nexus.domain.game.data.coupon.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
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
    @NotNull(message = "gameId는 필수입니다.")
    @Positive(message = "gameId는 1 이상이어야 합니다.")
    private Integer gameId;

    @Schema(example = "오픈 기념 쿠폰")
    @NotBlank(message = "쿠폰 이름은 필수입니다.")
    @Size(max = 255, message = "쿠폰 이름은 255자 이하여야 합니다.")
    private String name;

    @Schema(example = "오픈 기념 쿠폰입니다. 많이 받아가세요")
    @NotNull(message = "쿠폰 상세 설명은 필수입니다.")
    @Size(max = 255, message = "쿠폰 상세 설명은 255자 이하여야 합니다.")
    private String desc;

    @Schema(example = "WELCOME2026")
    @NotBlank(message = "쿠폰 코드는 필수입니다.")
    @Size(max = 255, message = "쿠폰 코드는 255자 이하여야 합니다.")
    private String code;

    @Schema(example = "[{\"currencyId\":1, \"amount\": 1000}]")
    @NotNull(message = "쿠폰 보상 정보는 필수입니다.")
    private JSONB rewards;

    @NotNull(message = "쿠폰 시작일은 필수입니다.")
    private OffsetDateTime useStartDate;

    @NotNull(message = "쿠폰 종료일은 필수입니다.")
    private OffsetDateTime useEndDate;

    @NotNull(message = "쿠폰 발행량은 필수입니다.")
    @PositiveOrZero(message = "쿠폰 발행량은 0 이상이어야 합니다.")
    private Long maxIssueCount;

    @NotNull(message = "유저당 사용 가능 개수는 필수입니다.")
    @Positive(message = "유저당 사용 가능 개수는 1 이상이어야 합니다.")
    private Integer useLimitPerUser;

    @NotBlank(message = "createdBy는 필수입니다.")
    @Size(max = 64, message = "createdBy는 64자 이하여야 합니다.")
    private String createdBy;

    @NotBlank(message = "updatedBy는 필수입니다.")
    @Size(max = 64, message = "updatedBy는 64자 이하여야 합니다.")
    private String updatedBy;

    @AssertTrue(message = "쿠폰 종료일은 시작일보다 같거나 이후여야 합니다.")
    public boolean isValidDateRange() {
        if (useStartDate == null || useEndDate == null) {
            return true;
        }
        return !useEndDate.isBefore(useStartDate);
    }
}
