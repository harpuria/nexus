package com.qwerty.nexus.global.dto;

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
public class RewardsDto {
    private int currencyId;
    private Long amount;
}
