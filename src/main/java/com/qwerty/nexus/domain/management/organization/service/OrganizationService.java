package com.qwerty.nexus.domain.management.organization.service;

import com.qwerty.nexus.domain.management.organization.dto.request.OrganizationCreateRequestDto;
import com.qwerty.nexus.domain.management.organization.dto.request.OrganizationUpdateRequestDto;
import com.qwerty.nexus.domain.management.organization.dto.response.OrganizationListResponseDto;
import com.qwerty.nexus.domain.management.organization.dto.response.OrganizationResponseDto;
import com.qwerty.nexus.domain.management.organization.entity.OrganizationEntity;
import com.qwerty.nexus.domain.management.organization.repository.OrganizationRepository;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.paging.PagingEntity;
import com.qwerty.nexus.global.paging.PagingRequestDto;
import com.qwerty.nexus.global.paging.PagingUtil;
import com.qwerty.nexus.global.response.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository repository;

    /**
     * Create organization information.
     *
     * @param dto organization create request payload
     * @return success or failure
     */
    @Transactional
    public Result<Void> createOrganization(OrganizationCreateRequestDto dto) {
        OrganizationEntity orgEntity = OrganizationEntity.builder()
                .orgNm(dto.getOrgNm())
                .orgCd(dto.getOrgCd())
                .logoPath(dto.getLogoPath())
                .createdBy(dto.getCreatedBy())
                .updatedBy(dto.getCreatedBy())
                .build();

        Integer insertedOrgId = repository.insertOrganization(orgEntity);
        if (insertedOrgId == null) {
            return Result.Failure.of("Organization create failed.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.CREATED);
    }

    /**
     * Update organization information.
     *
     * @param dto organization update request payload
     * @return success or failure
     */
    @Transactional
    public Result<Void> updateOrganization(OrganizationUpdateRequestDto dto) {
        // Check actor permission before update when auth/authorization is fully wired.
        OrganizationEntity orgEntity = OrganizationEntity.builder()
                .orgId(dto.getOrgId())
                .orgNm(dto.getOrgNm())
                .orgCd(dto.getOrgCd())
                .updatedBy(dto.getUpdatedBy())
                .build();

        int updatedRows = repository.updateOrganization(orgEntity);
        if (updatedRows > 0) {
            return Result.Success.of(null, "단체 정보 수정 성공.");
        }

        return Result.Failure.of("단체 정보 수정 실패.", ErrorCode.INTERNAL_ERROR.getCode());
    }

    /**
     * Delete organization information (logical delete).
     *
     * @param dto organization update request payload
     * @return success or failure
     */
    @Transactional
    public Result<Void> deleteOrganization(OrganizationUpdateRequestDto dto) {
        OrganizationEntity orgEntity = OrganizationEntity.builder()
                .orgId(dto.getOrgId())
                .isDel(dto.getIsDel())
                .updatedBy(dto.getUpdatedBy())
                .build();

        int deletedRows = repository.deleteOrganization(orgEntity);
        if (deletedRows <= 0) {
            return Result.Failure.of("Organization delete failed.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.DELETED);
    }

    /**
     * Get a single organization information.
     *
     * @param orgId organization primary key
     * @return organization data or failure
     */
    public Result<OrganizationResponseDto> getOrganization(int orgId) {
        Optional<OrganizationEntity> selectResult = Optional.ofNullable(repository.findByOrgId(orgId));
        if (selectResult.isPresent()) {
            return Result.Success.of(OrganizationResponseDto.from(selectResult.get()), "단체 정보 조회 완료.");
        }

        return Result.Failure.of("단체 정보 존재하지 않음.", ErrorCode.NOT_FOUND.getCode());
    }

    /**
     * List organizations.
     *
     * @param pagingDto paging request data
     * @return paged organization list
     */
    public Result<OrganizationListResponseDto> listOrganizations(PagingRequestDto pagingDto) {
        PagingEntity pagingEntity = PagingUtil.getPagingEntity(pagingDto);
        if (pagingEntity == null) {
            return Result.Failure.of("Paging parse failed.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        int validatedSize = pagingEntity.getSize();
        int safePage = pagingEntity.getPage();

        List<OrganizationEntity> selectResult = repository.findAllByPaging(pagingEntity);
        if (selectResult == null) {
            return Result.Failure.of("Organization list retrieval failed.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        List<OrganizationResponseDto> organizations = selectResult.stream()
                .map(OrganizationResponseDto::from)
                .toList();

        long totalCount = repository.countActiveOrganizations();
        int totalPages = validatedSize == 0 ? 0 : (int) Math.ceil((double) totalCount / validatedSize);
        boolean hasNext = safePage + 1 < totalPages;
        boolean hasPrevious = safePage > 0 && totalPages > 0;

        OrganizationListResponseDto response = OrganizationListResponseDto.builder()
                .organizations(organizations)
                .page(safePage)
                .size(validatedSize)
                .totalCount(totalCount)
                .totalPages(totalPages)
                .hasNext(hasNext)
                .hasPrevious(hasPrevious)
                .build();

        return Result.Success.of(response, ApiConstants.Messages.Success.RETRIEVED);
    }
}


