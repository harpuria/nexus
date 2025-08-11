package com.qwerty.nexus.domain.table.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TableUpdateRequestDto {
    @Schema(example = "1")
    private Integer tableId;

    @Schema(example = "GOODS_UPDATE")
    private String name;

    @Schema(example = "재화 담당 테이블 (수정)")
    private String description;

    @Schema(example = "admin")
    private String updatedBy;

    @Schema(example = "N")
    private String isDel;
}
