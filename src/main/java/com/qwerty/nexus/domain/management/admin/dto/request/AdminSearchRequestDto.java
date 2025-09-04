package com.qwerty.nexus.domain.management.admin.dto.request;

import com.qwerty.nexus.domain.management.admin.command.AdminSearchCommand;
import com.qwerty.nexus.global.extend.dto.PagingRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminSearchRequestDto extends PagingRequestDto {
    public AdminSearchCommand toCommand(){
        return AdminSearchCommand.builder()
                .sort(this.getSort())
                .page(this.getPage())
                .direction(this.getDirection())
                .keyword(this.getKeyword())
                .build();
    }
}
