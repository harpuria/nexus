package com.qwerty.nexus.domain.management.admin.command;

import com.qwerty.nexus.domain.management.admin.AdminRole;
import com.qwerty.nexus.domain.management.admin.dto.request.AdminCreateRequestDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminCreateCommand {
    private String loginId;
    private String loginPw;
    private String adminEmail;
    private String adminNm;
    private AdminRole adminRole;
    private String orgCd;
    private int orgId;

    public static AdminCreateCommand from(AdminCreateRequestDto dto){
        return AdminCreateCommand.builder()
                .loginId(dto.getLoginId())
                .loginPw(dto.getLoginPw())
                .adminEmail(dto.getAdminEmail())
                .adminNm(dto.getAdminNm())
                .adminRole(dto.getAdminRole())
                .orgId(dto.getOrgId())
                .build();
    }
}
