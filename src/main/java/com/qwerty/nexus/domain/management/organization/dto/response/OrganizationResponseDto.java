package com.qwerty.nexus.domain.management.organization.dto.response;

import com.qwerty.nexus.domain.management.organization.entity.OrganizationEntity;
import com.qwerty.nexus.global.paging.dto.BaseResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class OrganizationResponseDto extends BaseResponseDto {
    private Integer orgId;
    private String orgNm;
    private String orgCd;

    public static OrganizationResponseDto from(OrganizationEntity entity) {
        return OrganizationResponseDto.builder()
                .orgId(entity.getOrgId())
                .orgNm(entity.getOrgNm())
                .orgCd(entity.getOrgCd())
                .build();
    }
}
