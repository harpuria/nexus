package com.qwerty.nexus.domain.management.game.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qwerty.nexus.domain.management.game.GameStatus;
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
    private GameStatus status;

    @Schema(example = "N")
    private String isDel;

    @Schema(example = "admin")
    private String updatedBy;

    @Schema(example = "0.01")
    private String version;

    // no parameter
    @JsonIgnore
    private int gameId;
}
