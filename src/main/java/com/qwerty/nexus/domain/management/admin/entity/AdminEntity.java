package com.qwerty.nexus.domain.management.admin.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
public class AdminEntity {
    private Integer adminId;
    private Integer orgId;
    private Integer gameId;
    private String loginId;
    private String loginPw;
    private String adminRole;
    private String adminEmail;
    private String adminNm;
    private OffsetDateTime createdAt;
    private String createdBy;
    private OffsetDateTime updatedAt;
    private String updatedBy;
    private String isDel;
}
