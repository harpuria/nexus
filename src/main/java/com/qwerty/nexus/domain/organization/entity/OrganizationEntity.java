package com.qwerty.nexus.domain.organization.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrganizationEntity {
    private Integer orgId;
    private String orgNm;
    private String orgCd;
    private String logoPath;
    private String createdBy;
    private String updatedBy;
    private String isDel;
}
