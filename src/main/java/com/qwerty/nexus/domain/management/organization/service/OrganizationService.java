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

@Log4j2
@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    /**
     * 단체 생성
     * @param dto 생성할 단체 정보
     * @return 성공 혹은 실패
     */
    @Transactional
    public Result<Void> createOrganization(OrganizationCreateRequestDto dto) {
        OrganizationEntity organizationEntity = OrganizationEntity.builder()
                .orgNm(dto.getOrgNm())
                .orgCd(dto.getOrgCd())
                .logoPath(dto.getLogoPath())
                .createdBy(dto.getCreatedBy())
                .updatedBy(dto.getCreatedBy())
                .isDel("N")
                .build();

        Integer organizationId = organizationRepository.insertOrganization(organizationEntity);
        if (organizationId == null) {
            return Result.Failure.of("단체 생성 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.CREATED);
    }

    /**
     * 단체 정보 수정
     * @param dto 수정할 단체 정보
     * @return 성공 혹은 실패
     */
    @Transactional
    public Result<Void> updateOrganization(OrganizationUpdateRequestDto dto) {
        OrganizationEntity targetOrganization = organizationRepository.findByOrgId(dto.getOrgId());
        if (targetOrganization == null) {
            return Result.Failure.of("단체 정보 존재하지 않음.", ErrorCode.NOT_FOUND.getCode());
        }

        OrganizationEntity organizationEntity = OrganizationEntity.builder()
                .orgId(dto.getOrgId())
                .orgNm(dto.getOrgNm())
                .orgCd(dto.getOrgCd())
                .logoPath(dto.getLogoPath())
                .updatedBy(dto.getUpdatedBy())
                .build();

        int updatedRows = organizationRepository.updateOrganization(organizationEntity);
        if (updatedRows <= 0) {
            return Result.Failure.of("단체 정보 수정 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.UPDATED);
    }

    /**
     * 단체 삭제 (논리 삭제)
     * @param dto 삭제할 단체 정보
     * @return 성공 혹은 실패
     */
    @Transactional
    public Result<Void> deleteOrganization(OrganizationUpdateRequestDto dto) {
        OrganizationEntity targetOrganization = organizationRepository.findByOrgId(dto.getOrgId());
        if (targetOrganization == null) {
            return Result.Failure.of("단체 정보 존재하지 않음.", ErrorCode.NOT_FOUND.getCode());
        }

        OrganizationEntity organizationEntity = OrganizationEntity.builder()
                .orgId(dto.getOrgId())
                .isDel("Y")
                .updatedBy(dto.getUpdatedBy())
                .build();

        int deletedRows = organizationRepository.deleteOrganization(organizationEntity);
        if (deletedRows <= 0) {
            return Result.Failure.of("단체 삭제 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.DELETED);
    }

    /**
     * 단체 단건 조회
     * @param orgId 단체 PK
     * @return 단체 정보
     */
    public Result<OrganizationResponseDto> getOrganization(int orgId) {
        OrganizationEntity organizationEntity = organizationRepository.findByOrgId(orgId);
        if (organizationEntity == null) {
            return Result.Failure.of("단체 정보 존재하지 않음.", ErrorCode.NOT_FOUND.getCode());
        }

        return Result.Success.of(OrganizationResponseDto.from(organizationEntity), ApiConstants.Messages.Success.RETRIEVED);
    }

    /**
     * 단체 목록 조회
     * @param pagingRequestDto 페이징 요청 정보
     * @return 단체 목록
     */
    public Result<OrganizationListResponseDto> listOrganizations(PagingRequestDto pagingRequestDto) {
        PagingEntity pagingEntity = PagingUtil.getPagingEntity(pagingRequestDto);
        if (pagingEntity == null) {
            return Result.Failure.of("페이징 정보 변환 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        List<OrganizationEntity> organizationEntities = organizationRepository.findAllByPaging(pagingEntity);
        if (organizationEntities == null) {
            return Result.Failure.of("단체 목록 조회 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        List<OrganizationResponseDto> organizations = organizationEntities.stream()
                .map(OrganizationResponseDto::from)
                .toList();

        long totalCount = organizationRepository.countActiveOrganizations();
        int totalPages = (int) Math.ceil((double) totalCount / pagingEntity.getSize());

        OrganizationListResponseDto responseDto = OrganizationListResponseDto.builder()
                .organizations(organizations)
                .page(pagingEntity.getPage())
                .size(pagingEntity.getSize())
                .totalCount(totalCount)
                .totalPages(totalPages)
                .hasNext(pagingEntity.getPage() + 1 < totalPages)
                .hasPrevious(pagingEntity.getPage() > 0)
                .build();

        return Result.Success.of(responseDto, ApiConstants.Messages.Success.RETRIEVED);
    }
}
