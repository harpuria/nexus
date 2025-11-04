package com.qwerty.nexus.global.paging.command;

import com.qwerty.nexus.global.paging.dto.PagingRequestDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PagingCommand {
    private int page;
    private int size;
    private String sort;
    private String direction;
    private String keyword;

    public static PagingCommand from(PagingRequestDto dto){
        return PagingCommand.builder()
                .page(dto.getPage())
                .size(dto.getSize())
                .sort(dto.getSort())
                .direction(dto.getDirection())
                .keyword(dto.getKeyword())
                .build();
    }
}
