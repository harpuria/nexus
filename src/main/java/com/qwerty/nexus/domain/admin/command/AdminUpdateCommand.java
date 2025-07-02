package com.qwerty.nexus.domain.admin.command;

import lombok.Builder;
import lombok.Getter;

/**
 * 관리자 정보 수정용 Command
 */
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
