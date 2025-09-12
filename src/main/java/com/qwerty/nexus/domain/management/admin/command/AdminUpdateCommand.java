package com.qwerty.nexus.domain.management.admin.command;

import com.qwerty.nexus.domain.management.admin.dto.request.AdminUpdateRequestDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminUpdateCommand {
    private int adminId;
    private int gameId;
    private String loginPw;
    private String adminRole;
    private String adminEmail;
    private String adminNm;
    private String isDel;
    private String updatedBy;

    public static AdminUpdateCommand from(AdminUpdateRequestDto dto){
        return AdminUpdateCommand.builder()
                .adminId(dto.getAdminId())
                .gameId(dto.getGameId())
                .loginPw(dto.getLoginPw())
                .adminRole(dto.getAdminRole())
                .adminEmail(dto.getAdminEmail())
                .adminNm(dto.getAdminNm())
                .isDel(dto.getIsDel())
                .updatedBy(dto.getUpdatedBy())
                .build();
    }
}
