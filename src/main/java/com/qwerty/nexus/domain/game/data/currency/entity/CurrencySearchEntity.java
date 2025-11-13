package com.qwerty.nexus.domain.game.data.currency.entity;

import com.qwerty.nexus.global.paging.entity.PagingEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Getter
@Builder
public class CurrencySearchEntity {
    private Integer gameId;
    private Boolean includeDeleted;
    private PagingEntity paging;

    public boolean isIncludeDeleted() {
        return Boolean.TRUE.equals(includeDeleted);
    }

    public Optional<PagingEntity> getPagingOptional() {
        return Optional.ofNullable(paging);
    }
}
