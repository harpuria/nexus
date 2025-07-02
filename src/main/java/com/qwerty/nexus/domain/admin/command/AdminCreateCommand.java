package com.qwerty.nexus.domain.admin.command;

import lombok.Builder;
import lombok.Getter;

/**
 * 관리자 생성용 Command
 */
@Getter
@Builder
public class AdminCreateCommand {
    private String loginId;
    private String loginPw;
    private String adminEmail;
    private String adminNm;
    private String adminRole;
    private String orgNm;
    private String orgCd;
}
