package com.qwerty.nexus.domain.management.admin.dto.response;

import com.qwerty.nexus.domain.management.admin.entity.AdminEntity;
import com.qwerty.nexus.global.dto.BaseResponseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminResponseDto extends BaseResponseDTO {
    private Integer adminId;
    private Integer orgId;
    private Integer gameId;
    private String loginId;
    private String adminRole;
    private String adminEmail;
    private String adminNm;

    public void convertEntityToDto(AdminEntity admin) {
        this.setAdminId(admin.getAdminId());
        this.setOrgId(admin.getOrgId());
        this.setGameId(admin.getGameId());
        this.setLoginId(admin.getLoginId());
        this.setAdminRole(admin.getAdminRole());
        this.setAdminEmail(admin.getAdminEmail());
        this.setAdminNm(admin.getAdminNm());
        this.setCreatedAt(admin.getCreatedAt());
        this.setCreatedBy(admin.getCreatedBy());
        this.setUpdatedAt(admin.getUpdatedAt());
        this.setUpdatedBy(admin.getUpdatedBy());
        this.setIsDel(admin.getIsDel());
    }
}
