package com.qwerty.nexus.global.paging.entity;

import com.qwerty.nexus.global.paging.command.PagingCommand;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PagingEntity {
    private int page;
    private int size;
    private String sort;
    private String direction;
    private String keyword;

    public static PagingEntity from(PagingCommand command) {
        return PagingEntity.builder()
                .page(command.getPage())
                .size(command.getSize())
                .sort(command.getSort())
                .direction(command.getDirection())
                .keyword(command.getKeyword())
                .build();
    }
}
