package com.qwerty.nexus.domain.organization.dto.request;

import lombok.Data;
import org.jooq.generated.tables.pojos.Admin;
import org.jooq.generated.tables.records.OrganizationRecord;

@Data
public class OrganizationRequestDTO{
    private Integer orgId;
    private String orgNm;
    private String orgCd;
    private String createdBy;
    private String updatedBy;
    private String isDel;

    private Admin admin;

    // jOOQ Record Type 으로 변환하는 메서드
    public OrganizationRecord toAdminRecord() {
        OrganizationRecord record = new OrganizationRecord();
        record.setOrgId(this.orgId);
        record.setOrgNm(this.orgNm);
        record.setOrgCd(this.orgCd);
        record.setCreatedBy(this.createdBy);
        record.setUpdatedBy(this.updatedBy);
        record.setIsDel(this.isDel);
        return record;
    }
}
