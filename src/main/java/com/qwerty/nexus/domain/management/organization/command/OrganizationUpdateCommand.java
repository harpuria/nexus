package com.qwerty.nexus.domain.management.organization.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrganizationUpdateCommand {
    private int orgId;
    private String orgNm;
    private String orgCd;
    private String updatedBy;
}
