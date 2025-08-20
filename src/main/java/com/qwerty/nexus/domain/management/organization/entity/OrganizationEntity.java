package com.qwerty.nexus.domain.management.organization.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
public class OrganizationEntity {
    private Integer orgId;
    private String orgNm;
    private String orgCd;
    private String logoPath;
    private OffsetDateTime createdAt;
    private String createdBy;
    private OffsetDateTime updatedAt;
    private String updatedBy;
    private String isDel;
}
