package com.qwerty.nexus.domain.management.game.dto.request;

import com.qwerty.nexus.domain.management.game.command.GameCreateCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GameCreateRequestDto {
    @Schema(example = "1")
    private int orgId;

    @Schema(example = "그리즐리키우기")
    private String name;

    @Schema(example = "admin")
    private String createBy;

    // Service 전달 파라미터로 쓸 Command 객체 변환
    public GameCreateCommand toCommand(){
        return GameCreateCommand.builder()
                .orgId(this.orgId)
                .name(this.name)
                .createBy(this.createBy)
                .build();
    }
}
