package com.qwerty.nexus.domain.table.dto.request;

import com.qwerty.nexus.domain.table.command.TableCreateCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TableCreateRequestDto {
    @Schema(example = "1")
    private Integer gameId;

    @Schema(example = "GOODS")
    private String name;

    @Schema(example = "재화를 담당하는 테이블")
    private String description;

    @Schema(example = "admin")
    private String createdBy;

    // Service 전달 파라미터로 쓸 Command 객체 변환
    public TableCreateCommand toTableCreateCommand(){
        return TableCreateCommand.builder()
                .gameId(this.gameId)
                .name(this.name)
                .description(this.description)
                .createdBy(this.createdBy)
                .build();
    }
}
