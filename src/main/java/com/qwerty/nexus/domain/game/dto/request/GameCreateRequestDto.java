package com.qwerty.nexus.domain.game.dto.request;

import com.qwerty.nexus.domain.game.command.GameCreateCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GameCreateRequestDto {
    private String name;
    private String createBy;

    // Service 전달 파라미터로 쓸 Command 객체 변환
    public GameCreateCommand toGameCommand(){
        return GameCreateCommand.builder()
                .name(this.name)
                .createBy(this.createBy)
                .build();
    }
}
