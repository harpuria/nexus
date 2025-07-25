package com.qwerty.nexus.domain.gameUser.dto.request;

import com.qwerty.nexus.domain.game.command.GameCreateCommand;
import com.qwerty.nexus.domain.gameUser.command.GameUserCreateCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GameUserCreateRequestDto {
    private Integer gameId;
    private String userLId;
    private String userLPw;
    private String nickname;
    private String loginType;
    private String device;
    private String createdBy;

    // Service 전달 파라미터로 쓸 Command 객체 변환
    public GameUserCreateCommand toGameCommand(){
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
