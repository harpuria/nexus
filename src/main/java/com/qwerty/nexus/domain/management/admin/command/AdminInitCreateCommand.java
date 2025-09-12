package com.qwerty.nexus.domain.management.admin.command;

import com.qwerty.nexus.domain.management.admin.dto.request.AdminInitCreateRequestDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminInitCreateCommand {
    private String loginId;
    private String loginPw;
    private String adminEmail;
    private String adminNm;
    private String adminRole;
    private String orgNm;
    private String orgCd;
    private int orgId;

    public static AdminInitCreateCommand from(AdminInitCreateRequestDto dto){
        return AdminInitCreateCommand.builder()
                .loginId(dto.getLoginId())
                .loginPw(dto.getLoginPw())
                .adminEmail(dto.getAdminEmail())
                .adminNm(dto.getAdminNm())
                .adminRole(dto.getAdminRole())
                .orgNm(dto.getOrgNm())
                .orgCd(dto.getOrgCd())
                .build();
    }
}
