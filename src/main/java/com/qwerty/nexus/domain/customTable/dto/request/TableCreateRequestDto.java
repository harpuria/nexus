package com.qwerty.nexus.domain.customTable.dto.request;

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
}
