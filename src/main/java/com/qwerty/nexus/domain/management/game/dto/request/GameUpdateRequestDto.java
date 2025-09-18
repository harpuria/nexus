package com.qwerty.nexus.domain.management.game.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qwerty.nexus.domain.management.game.command.GameUpdateCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GameUpdateRequestDto {
    @Schema(example = "그리즐리키우기(변경)")
    private String name;

    @Schema(example = "OPERATING")
    private String status;

    @Schema(example = "N")
    private String isDel;

    @Schema(example = "admin")
    private String updateBy;

    // no parameter
    @JsonIgnore
    private int gameId;
}
