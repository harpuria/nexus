package com.qwerty.nexus.domain.management.organization.command;

import com.qwerty.nexus.domain.management.organization.dto.request.OrganizationUpdateRequestDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrganizationUpdateCommand {
    private int orgId;
    private String orgNm;
    private String orgCd;
    private String updatedBy;

    public static OrganizationUpdateCommand from(OrganizationUpdateRequestDto dto){
        return OrganizationUpdateCommand.builder()
                .orgId(dto.getOrgId())
                .orgNm(dto.getOrgNm())
                .orgCd(dto.getOrgCd())
                .updatedBy(dto.getUpdateBy())
                .build();
    }
}
