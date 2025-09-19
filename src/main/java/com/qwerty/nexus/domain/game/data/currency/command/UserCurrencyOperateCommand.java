package com.qwerty.nexus.domain.game.data.currency.command;

import com.qwerty.nexus.domain.game.data.currency.dto.request.UserCurrencyOperateRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
public class UserCurrencyOperateCommand {
    private Integer userCurrencyId;
    private Integer currencyId;
    private Integer userId;
    private String operation;   // 연산자
    private Long operateAmount; // 연산값
    private Long currentAmount; // 현재재화값 (DB 비교)
    private String updatedBy;

    public static UserCurrencyOperateCommand from(UserCurrencyOperateRequestDto dto){
        return UserCurrencyOperateCommand.builder()
                .userCurrencyId(dto.getUserCurrencyId())
                .currencyId(dto.getCurrencyId())
                .userId(dto.getUserId())
                .operation(dto.getOperation())
                .operateAmount(dto.getOperateAmount())
                .currentAmount(dto.getCurrentAmount())
                .updatedBy(dto.getUpdatedBy())
                .build();
    }
}
