package com.qwerty.nexus.domain.management.organization.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OrganizationListResponseDto {
    private List<OrganizationResponseDto> organizations;
    private int page;
    private int size;
    private long totalCount;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
}
