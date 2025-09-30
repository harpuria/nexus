package com.qwerty.nexus.domain.management.admin.command;

import com.qwerty.nexus.domain.management.admin.dto.request.AdminLoginRequestDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminLoginCommand {
    public static AdminLoginCommand from(AdminLoginRequestDto dto){
        return AdminLoginCommand.builder().build();
    }
}
