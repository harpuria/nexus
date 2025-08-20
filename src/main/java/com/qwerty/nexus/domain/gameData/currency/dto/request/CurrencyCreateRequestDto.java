package com.qwerty.nexus.domain.gameData.currency.dto.request;

import com.qwerty.nexus.domain.gameData.currency.command.CurrencyCreateCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CurrencyCreateRequestDto {
    @Schema(example = "1")
    private Integer gameId;

    @Schema(example = "골드")
    private String name;

    @Schema(example = "인게임에서 쓰는 골드 재화")
    private String desc;

    @Schema(example = "1000000000")
    private Long maxAmount;

    @Schema(example = "admin")
    private String createdBy;

    // Service 에서 쓸 command 객체 변환
    public CurrencyCreateCommand toCommand(){
        return CurrencyCreateCommand.builder()
                .gameId(this.gameId)
                .name(this.name)
                .desc(this.desc)
                .maxAmount(this.maxAmount)
                .createdBy(this.createdBy)
                .build();
    }
}
