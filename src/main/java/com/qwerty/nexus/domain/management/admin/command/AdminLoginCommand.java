package com.qwerty.nexus.domain.management.admin.command;

import com.qwerty.nexus.domain.management.admin.dto.request.AdminLoginRequestDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminLoginCommand {
    private String loginId;
    private String loginPw;

    public static AdminLoginCommand from(AdminLoginRequestDto dto){
        return AdminLoginCommand.builder()
                .loginId(dto.getLoginId())
                .loginPw(dto.getLoginPw())
                .build();
    }
}
