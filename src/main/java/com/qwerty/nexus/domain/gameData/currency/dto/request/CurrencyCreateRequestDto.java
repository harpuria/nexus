package com.qwerty.nexus.domain.gameData.currency.dto.request;

import com.qwerty.nexus.domain.gameData.currency.command.CurrencyCreateCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CurrencyCreateRequestDto {
    private Integer gameId;
    private String name;
    private String desc;
    private Long maxAmount;
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
