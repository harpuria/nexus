package com.qwerty.nexus.organization;

import com.qwerty.nexus.global.dto.BaseResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jooq.generated.tables.pojos.Admin;
import org.jooq.generated.tables.pojos.Organization;

import java.time.OffsetDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrganizationResponseDTO extends BaseResponseDTO {
    private Integer orgId;
    private String orgNm;
    private String orgCd;

    public OrganizationResponseDTO convertPojoToDTO(Organization org) {
        this.setOrgId(org.getOrgId());
        this.setOrgNm(org.getOrgNm());
        this.setOrgCd(org.getOrgCd());
        this.setCreatedAt(org.getCreatedAt());
        this.setCreatedBy(org.getCreatedBy());
        this.setUpdatedAt(org.getUpdatedAt());
        this.setUpdatedBy(org.getUpdatedBy());
        this.setIsDel(org.getIsDel());
        return this;
    }
}
