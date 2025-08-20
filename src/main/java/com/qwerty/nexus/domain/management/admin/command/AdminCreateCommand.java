package com.qwerty.nexus.domain.management.admin.command;

import lombok.Builder;
import lombok.Getter;

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
    private int orgId;
}
