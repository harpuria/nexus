package com.qwerty.nexus.domain.management.organization.dto.response;

import com.qwerty.nexus.domain.management.organization.entity.OrganizationEntity;
import com.qwerty.nexus.global.dto.BaseResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrganizationResponseDto extends BaseResponseDTO {
    private Integer orgId;
    private String orgNm;
    private String orgCd;

    public OrganizationResponseDto convertEntityToDTO(OrganizationEntity org) {
        this.setOrgId(org.getOrgId());
        this.setOrgNm(org.getOrgNm());
        this.setOrgCd(org.getOrgCd());
        this.setCreatedBy(org.getCreatedBy());
        this.setUpdatedBy(org.getUpdatedBy());
        this.setIsDel(org.getIsDel());
        return this;
    }
}
