package com.qwerty.nexus.domain.game.dto.request;

import com.qwerty.nexus.domain.game.GameStatus;
import com.qwerty.nexus.domain.game.command.GameCreateCommand;
import com.qwerty.nexus.domain.game.command.GameUpdateCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GameUpdateRequestDto {
    @Schema(example = "그리글리키우기")
    private String name;

    @Schema(example = "OPERATING")
    private String status;

    @Schema(example = "N")
    private String isDel;

    @Schema(example = "admin")
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
