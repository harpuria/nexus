package com.qwerty.nexus.domain.management.organization.dto.response;

import com.qwerty.nexus.domain.management.organization.entity.OrganizationEntity;
import com.qwerty.nexus.global.dto.BaseResponseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationResponseDto extends BaseResponseDTO {
    private Integer orgId;
    private String orgNm;
    private String orgCd;

    public void convertEntityToDto(OrganizationEntity org) {
        this.setOrgId(org.getOrgId());
        this.setOrgNm(org.getOrgNm());
        this.setOrgCd(org.getOrgCd());
        this.setCreatedAt(org.getCreatedAt());
        this.setCreatedBy(org.getCreatedBy());
        this.setUpdatedAt(org.getUpdatedAt());
        this.setUpdatedBy(org.getUpdatedBy());
        this.setIsDel(org.getIsDel());
    }
}
