package com.qwerty.nexus.domain.management.admin.entity;

import com.qwerty.nexus.domain.management.admin.AdminRole;
import lombok.Builder;
import lombok.Getter;
import org.jooq.generated.tables.records.AdminRecord;

import java.time.OffsetDateTime;

@Getter
@Builder
public class AdminEntity {
    private Integer adminId;
    private Integer orgId;
    private Integer gameId;
    private String loginId;
    private String loginPw;
    private AdminRole adminRole;
    private String adminEmail;
    private String adminNm;
    private OffsetDateTime createdAt;
    private String createdBy;
    private OffsetDateTime updatedAt;
    private String updatedBy;
    private String isDel;

    public static AdminEntity from(AdminRecord record){
        return AdminEntity.builder()
                .adminId(record.getAdminId())
                .orgId(record.getOrgId())
                .gameId(record.getGameId())
                .loginId(record.getLoginId())
                .loginPw(record.getLoginPw())
                .adminRole(record.getAdminRole())
                .adminEmail(record.getAdminEmail())
                .adminNm(record.getAdminNm())
                .createdAt(record.getCreatedAt())
                .createdBy(record.getCreatedBy())
                .updatedAt(record.getUpdatedAt())
                .updatedBy(record.getUpdatedBy())
                .isDel(record.getIsDel())
                .build();
    }
}
