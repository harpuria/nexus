package com.qwerty.nexus.domain.game.user.dto.request;

import com.qwerty.nexus.domain.game.user.command.GameUserCreateCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GameUserCreateRequestDto {
    @Schema(example = "1")
    private Integer gameId;

    @Schema(example = "userTest")
    private String userLId;

    @Schema(example = "userTest")
    private String userLPw;

    @Schema(example = "테스트유저")
    private String nickname;

    @Schema(example = "GOOGLE")
    private String loginType;

    @Schema(example = "SAMSUNG")
    private String device;

    @Schema(example = "userTest")
    private String createdBy;

    // Service 전달 파라미터로 쓸 Command 객체 변환
    public GameUserCreateCommand toCommand(){
        return GameUserCreateCommand.builder()
                .gameId(this.gameId)
                .userLId(this.userLId)
                .userLPw(this.userLPw)
                .nickname(this.nickname)
                .loginType(this.loginType)
                .device(this.device)
                .createdBy(this.createdBy)
                .build();
    }
}
