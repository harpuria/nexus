package com.qwerty.nexus.domain.management.admin.command;

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
}
