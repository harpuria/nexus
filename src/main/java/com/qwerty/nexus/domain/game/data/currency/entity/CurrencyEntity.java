package com.qwerty.nexus.domain.game.data.currency.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
public class CurrencyEntity {
    private Integer currencyId;
    private Integer gameId;
    private String name;
    private String desc;
    private Long maxAmount;
    private OffsetDateTime createdAt;
    private String createdBy;
    private OffsetDateTime updatedAt;
    private String updatedBy;
    private String isDel;
}
