package com.qwerty.nexus.domain.game.dto.request;

import com.qwerty.nexus.domain.game.command.GameCreateCommand;
import com.qwerty.nexus.domain.game.command.GameUpdateCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GameUpdateRequestDto {
    private String name;
    private String status;
    private String isDel;
    private String updateBy;

    // Service 전달 파라미터로 쓸 Command 객체 변환
    public GameUpdateCommand toGameCommand(){
        return GameUpdateCommand.builder()
                .name(this.name)
                .status(this.status)
                .isDel(this.isDel)
                .updateBy(this.updateBy)
                .build();
    }
}
