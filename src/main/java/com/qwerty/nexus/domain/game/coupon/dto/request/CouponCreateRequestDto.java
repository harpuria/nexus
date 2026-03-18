package com.qwerty.nexus.domain.game.coupon.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import com.qwerty.nexus.domain.game.coupon.TimeLimitType;
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
    @NotNull(message = "gameId는 필수입니다.")
    @Positive(message = "gameId는 1 이상이어야 합니다.")
    @Schema(example = "1")
    private Integer gameId;

    @NotBlank(message = "쿠폰 이름은 필수입니다.")
    @Size(max = 255, message = "쿠폰 이름은 255자 이하여야 합니다.")
    @Schema(example = "테스트 데이터")
    private String name;

    @NotNull(message = "쿠폰 상세 설명은 필수입니다.")
    @Size(max = 255, message = "쿠폰 상세 설명은 255자 이하여야 합니다.")
    @Schema(example = "예시 설명입니다.")
    private String desc;

    @NotBlank(message = "쿠폰 코드는 필수입니다.")
    @Size(max = 255, message = "쿠폰 코드는 255자 이하여야 합니다.")
    @Schema(example = "TEST_CODE_001")
    private String code;

    @NotNull(message = "쿠폰 보상 정보는 필수입니다.")
    @Schema(example = "[{\"itemId\":1,\"qty\":100}]")
    private JSONB rewards;

    @Schema(example = "LIMITED")
    private TimeLimitType timeLimitType;

    @Schema(example = "2026-03-18T09:00:00+09:00")
    private OffsetDateTime useStartDate;
    @Schema(example = "2026-03-18T09:00:00+09:00")
    private OffsetDateTime useEndDate;

    @NotNull(message = "쿠폰 발행량은 필수입니다.")
    @PositiveOrZero(message = "쿠폰 발행량은 0 이상이어야 합니다.")
    @Schema(example = "100")
    private Long maxIssueCount;

    @NotNull(message = "유저당 사용 가능 개수는 필수입니다.")
    @Positive(message = "유저당 사용 가능 개수는 1 이상이어야 합니다.")
    @Schema(example = "1")
    private Integer useLimitPerUser;

    @NotBlank(message = "createdBy는 필수입니다.")
    @Size(max = 64, message = "createdBy는 64자 이하여야 합니다.")
    @Schema(example = "admin")
    private String createdBy;

    @NotBlank(message = "updatedBy는 필수입니다.")
    @Size(max = 64, message = "updatedBy는 64자 이하여야 합니다.")
    @Schema(example = "admin")
    private String updatedBy;

    @AssertTrue(message = "쿠폰 종료일은 시작일보다 같거나 이후여야 합니다.")
    public boolean isValidDateRange() {
        return useStartDate == null || useEndDate == null || !useEndDate.isBefore(useStartDate);
    }
}
