package com.qwerty.nexus.domain.game.attendance.dto.response;

import com.qwerty.nexus.domain.game.attendance.entity.AttendanceRewardEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AttendanceRewardResponseDto {
    private Integer dayNo;
    private Integer rewardSeq;
    private String rewardType;
    private Integer itemId;
    private String itemCode;
    private Long rewardQty;
    private String isBonus;

    public static AttendanceRewardResponseDto from(AttendanceRewardEntity entity) {
        return AttendanceRewardResponseDto.builder()
                .dayNo(entity.getDayNo())
                .rewardSeq(entity.getRewardSeq())
                .rewardType(entity.getRewardType())
                .itemId(entity.getItemId())
                .itemCode(entity.getItemCode())
                .rewardQty(entity.getRewardQty() == null ? null : entity.getRewardQty().longValue())
                .isBonus(entity.getIsBonus())
                .build();
    }
}
