package com.qwerty.nexus.domain.gameUser.dto.request;

import com.qwerty.nexus.domain.gameUser.command.GameUserCreateCommand;
import com.qwerty.nexus.domain.gameUser.command.GameUserUpdateCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
public class GameUserUpdateRequestDto {
    private Integer userId;
    private Integer gameId;
    private String userLId;
    private String userLPw;
    private String nickname;
    private String loginType;
    private String device;
    private OffsetDateTime blockStartDate;
    private OffsetDateTime blockEndDate;
    private String blockReason;
    private String isWithdrawal;
    private OffsetDateTime withdrawalDate;
    private String withdrawalReason;
    private String updatedBy;
    private String isDel;

    // Service 전달 파라미터로 쓸 Command 객체 변환
    public GameUserUpdateCommand toGameCommand(){
        return GameUserUpdateCommand.builder()
                .userId(this.userId)
                .gameId(this.gameId)
                .userLId(this.userLId)
                .userLPw(this.userLPw)
                .nickname(this.nickname)
                .loginType(this.loginType)
                .device(this.device)
                .blockStartDate(this.blockStartDate)
                .blockEndDate(this.blockEndDate)
                .blockReason(this.blockReason)
                .isWithdrawal(this.isWithdrawal)
                .withdrawalDate(this.withdrawalDate)
                .withdrawalReason(this.withdrawalReason)
                .updatedBy(this.updatedBy)
                .isDel(this.isDel)
                .build();
    }

}
