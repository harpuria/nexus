package com.qwerty.nexus.domain.organization.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrganizationUpdateCommand {
    private String orgNm;
    private String orgCd;
    private String updatedBy;

    // Service 전달 파라미터로 쓸 Command 객체 변환
    public OrganizationUpdateCommand toOrganizationCommand() {
        return OrganizationUpdateCommand.builder()
                .orgNm(this.orgNm)
                .orgCd(this.orgCd)
                .updatedBy(this.updatedBy)
                .build();
    }
}
