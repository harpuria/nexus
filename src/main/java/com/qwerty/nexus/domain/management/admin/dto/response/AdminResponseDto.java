package com.qwerty.nexus.domain.management.admin.dto.response;

import com.qwerty.nexus.domain.management.admin.AdminRole;
import com.qwerty.nexus.domain.management.admin.entity.AdminEntity;
import com.qwerty.nexus.global.paging.dto.BaseResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class AdminResponseDto extends BaseResponseDto {
    private Integer adminId;
    private Integer orgId;
    private Integer gameId;
    private String loginId;
    private AdminRole adminRole;
    private String adminEmail;
    private String adminNm;

    public static AdminResponseDto from(AdminEntity entity){
        return AdminResponseDto.builder()
                .adminId(entity.getAdminId())
                .orgId(entity.getOrgId())
                .gameId(entity.getGameId())
                .loginId(entity.getLoginId())
                .adminRole(entity.getAdminRole())
                .adminEmail(entity.getAdminEmail())
                .adminNm(entity.getAdminNm())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .isDel(entity.getIsDel())
                .build();
    }
}
