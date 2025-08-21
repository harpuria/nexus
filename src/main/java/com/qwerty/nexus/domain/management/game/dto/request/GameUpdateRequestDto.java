package com.qwerty.nexus.domain.management.game.dto.request;

import com.qwerty.nexus.domain.management.game.command.GameUpdateCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GameUpdateRequestDto {
    private int gameId;

    @Schema(example = "그리즐리키우기(변경)")
    private String name;

    @Schema(example = "OPERATING")
    private String status;

    @Schema(example = "N")
    private String isDel;

    @Schema(example = "admin")
    private String updateBy;

    public GameUpdateCommand toCommand(){
        return GameUpdateCommand.builder()
                .gameId(this.gameId)
                .name(this.name)
                .status(this.status)
                .isDel(this.isDel)
                .updatedBy(this.updateBy)
                .build();
    }
}
