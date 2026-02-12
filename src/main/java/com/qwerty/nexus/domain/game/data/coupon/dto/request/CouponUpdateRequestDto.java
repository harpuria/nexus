package com.qwerty.nexus.domain.game.data.coupon.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Pattern;
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
public class CouponUpdateRequestDto {
    @Positive(message = "gameId는 1 이상이어야 합니다.")
    private Integer gameId;

    @Size(max = 255, message = "쿠폰 이름은 255자 이하여야 합니다.")
    private String name;

    @Size(max = 255, message = "쿠폰 상세설명은 255자 이하여야 합니다.")
    private String desc;

    @Size(max = 255, message = "쿠폰 코드는 255자 이하여야 합니다.")
    private String code;

    private JSONB rewards;
    private OffsetDateTime useStartDate;
    private OffsetDateTime useEndDate;

    @PositiveOrZero(message = "쿠폰 발행량은 0 이상이어야 합니다.")
    private Long maxIssueCount;

    @Positive(message = "유저당 사용 가능 개수는 1 이상이어야 합니다.")
    private Integer useLimitPerUser;

    @Size(max = 64, message = "updatedBy는 64자 이하여야 합니다.")
    private String updatedBy;

    @Pattern(regexp = "^[YN]$", message = "isDel 값은 Y 또는 N 이어야 합니다.")
    private String isDel;

    @JsonIgnore
    private Integer couponId;

    @AssertTrue(message = "쿠폰 종료일은 시작일보다 같거나 이후여야 합니다.")
    public boolean isValidDateRange() {
        if (useStartDate == null || useEndDate == null) {
            return true;
        }
        return !useEndDate.isBefore(useStartDate);
    }
}
