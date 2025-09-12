package com.qwerty.nexus.domain.management.admin.command;

import com.qwerty.nexus.domain.management.admin.dto.request.AdminSearchRequestDto;
import com.qwerty.nexus.global.extend.command.PagingCommand;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class AdminSearchCommand extends PagingCommand {
    public static AdminSearchCommand from(AdminSearchRequestDto dto){
        return AdminSearchCommand.builder()
                .sort(dto.getSort())
                .page(dto.getPage())
                .size(dto.getSize())
                .direction(dto.getDirection())
                .keyword(dto.getKeyword())
                .build();
    }
}
