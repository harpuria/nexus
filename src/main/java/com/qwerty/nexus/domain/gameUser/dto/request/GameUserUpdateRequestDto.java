package com.qwerty.nexus.domain.gameUser.dto.request;

import com.qwerty.nexus.domain.gameUser.command.GameUserUpdateCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
public class GameUserUpdateRequestDto {
    @Schema(example = "1")
    private Integer userId;

    @Schema(example = "1")
    private Integer gameId;

    @Schema(example = "userTest")
    private String userLId;

    @Schema(example = "userTest")
    private String userLPw;

    @Schema(example = "수정유저테스트")
    private String nickname;

    @Schema(example = "APPLE")
    private String loginType;

    @Schema(example = "iPhone")
    private String device;

    @Schema(example = "2025-08-10")
    private OffsetDateTime blockStartDate;

    @Schema(example = "2025-08-15")
    private OffsetDateTime blockEndDate;

    @Schema(example = "블럭 사유")
    private String blockReason;

    @Schema(example = "Y")
    private String isWithdrawal;

    @Schema(example = "2025-08-11")
    private OffsetDateTime withdrawalDate;

    @Schema(example = "게임이 재미가 없어서")
    private String withdrawalReason;

    @Schema(example = "userTest")
    private String updatedBy;

    @Schema(example = "N")
    private String isDel;

    // Service 전달 파라미터로 쓸 Command 객체 변환
    public GameUserUpdateCommand toCommand(){
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
