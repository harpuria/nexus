package com.qwerty.nexus.domain.organization.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrganizationCreateCommand {
    private String orgNm;
    private String orgCd;
    private String createBy;
    private String updateBy;
}
