package com.qwerty.nexus.admin;

import lombok.Data;
import org.jooq.generated.tables.pojos.Organization;
import org.jooq.generated.tables.records.AdminRecord;
import org.jooq.generated.tables.records.OrganizationRecord;

import java.time.OffsetDateTime;

@Data
public class AdminRequestDTO {
    private Integer adminId;
    private Integer orgId;
    private Integer gameId;
    private String loginId;
    private String loginPw;
    private String adminRole;
    private String adminEmail;
    private String adminNm;
    private String isApprove;
    private String createdBy;
    private String updatedBy;
    private String isDel;

    private Organization organization;

    // jOOQ Record Type 으로 변환하는 메서드
    public AdminRecord toAdminRecord() {
        AdminRecord record = new AdminRecord();
        record.setAdminId(this.adminId);
        record.setOrgId(this.orgId);
        record.setGameId(this.gameId);
        record.setLoginId(this.loginId);
        record.setLoginPw(this.loginPw);
        record.setAdminRole(this.adminRole);
        record.setAdminEmail(this.adminEmail);
        record.setAdminNm(this.adminNm);
        record.setIsApprove(this.isApprove);
        record.setCreatedBy(this.createdBy);
        record.setUpdatedBy(this.updatedBy);
        record.setIsDel(this.isDel);
        return record;
    }
}
