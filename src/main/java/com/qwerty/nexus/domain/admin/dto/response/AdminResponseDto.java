package com.qwerty.nexus.domain.admin.dto.response;

import com.qwerty.nexus.global.dto.BaseResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jooq.generated.tables.records.AdminRecord;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdminResponseDto extends BaseResponseDTO {
    private Integer adminId;
    private Integer orgId;
    private Integer gameId;
    private String loginId;
    private String loginPw;
    private String adminRole;
    private String adminEmail;
    private String adminNm;
    private String isApprove;

    public AdminResponseDto convertPojoToDTO(AdminRecord admin) {
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
