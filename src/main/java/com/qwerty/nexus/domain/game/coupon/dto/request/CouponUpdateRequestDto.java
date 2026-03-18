package com.qwerty.nexus.domain.game.coupon.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qwerty.nexus.domain.game.coupon.TimeLimitType;
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
    @Schema(example = "1")
    private Integer gameId;

    @Size(max = 255, message = "쿠폰 이름은 255자 이하여야 합니다.")
    @Schema(example = "테스트 데이터")
    private String name;

    @Size(max = 255, message = "쿠폰 상세설명은 255자 이하여야 합니다.")
    @Schema(example = "예시 설명입니다.")
    private String desc;

    @Size(max = 255, message = "쿠폰 코드는 255자 이하여야 합니다.")
    @Schema(example = "TEST_CODE_001")
    private String code;

    @Schema(example = "[{\"itemId\":1,\"qty\":100}]")
    private JSONB rewards;
    @Schema(example = "2026-03-18T09:00:00+09:00")
    private OffsetDateTime useStartDate;
    @Schema(example = "2026-03-18T09:00:00+09:00")
    private OffsetDateTime useEndDate;

    @PositiveOrZero(message = "쿠폰 발행량은 0 이상이어야 합니다.")
    @Schema(example = "100")
    private Long maxIssueCount;

    @Positive(message = "유저당 사용 가능 개수는 1 이상이어야 합니다.")
    @Schema(example = "1")
    private Integer useLimitPerUser;

    @Schema(example = "LIMITED")
    private TimeLimitType timeLimitType;

    @Size(max = 64, message = "updatedBy는 64자 이하여야 합니다.")
    @Schema(example = "admin")
    private String updatedBy;

    @Pattern(regexp = "^[YN]$", message = "isDel 값은 Y 또는 N 이어야 합니다.")
    @Schema(example = "N")
    private String isDel;

    @JsonIgnore
    @Schema(hidden = true)
    private Integer couponId;

    @AssertTrue(message = "쿠폰 종료일은 시작일보다 같거나 이후여야 합니다.")
    public boolean isValidDateRange() {
        return useStartDate == null || useEndDate == null || !useEndDate.isBefore(useStartDate);
    }
}
