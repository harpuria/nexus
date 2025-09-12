package com.qwerty.nexus.domain.game.user.command;

import com.qwerty.nexus.domain.game.user.dto.request.GameUserLoginRequestDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameUserLoginRequestCommand {
    public GameUserLoginRequestCommand from(GameUserLoginRequestDto dto){
        return GameUserLoginRequestCommand.builder().build();
    }
}
