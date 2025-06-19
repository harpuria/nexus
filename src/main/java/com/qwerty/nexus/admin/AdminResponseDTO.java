package com.qwerty.nexus.admin;

import com.qwerty.nexus.global.dto.BaseResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jooq.generated.tables.pojos.Admin;
import org.jooq.generated.tables.records.AdminRecord;

import java.time.OffsetDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdminResponseDTO extends BaseResponseDTO {
    private Integer adminId;
    private Integer orgId;
    private Integer gameId;
    private String loginId;
    private String loginPw;
    private String adminRole;
    private String adminEmail;
    private String adminNm;
    private String isApprove;

    public AdminResponseDTO convertPojoToDTO(AdminRecord admin) {
        this.setAdminId(admin.getAdminId());
        this.setOrgId(admin.getOrgId());
        this.setGameId(admin.getGameId());
        this.setLoginId(admin.getLoginId());
        this.setLoginPw(admin.getLoginPw());
        this.setAdminRole(admin.getAdminRole());
        this.setAdminEmail(admin.getAdminEmail());
        this.setAdminNm(admin.getAdminNm());
        this.setIsApprove(admin.getIsApprove());
        this.setCreatedAt(admin.getCreatedAt());
        this.setCreatedBy(admin.getCreatedBy());
        this.setUpdatedAt(admin.getUpdatedAt());
        this.setUpdatedBy(admin.getUpdatedBy());
        this.setIsDel(admin.getIsDel());
        return this;
    }
}
