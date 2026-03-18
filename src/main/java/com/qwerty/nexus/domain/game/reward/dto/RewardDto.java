package com.qwerty.nexus.domain.game.reward.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 공통 보상 DTO (JSONB 타입을 DTO 타입으로 변경할 때 사용)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RewardDto {
    @Schema(example = "1")
    private Integer itemId;
    @Schema(example = "TEST_CODE_001")
    private String itemCode;
    @Schema(example = "100")
    private Long qty;
}
