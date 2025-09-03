package com.qwerty.nexus.domain.management.organization.service;

import com.qwerty.nexus.domain.management.organization.command.OrganizationUpdateCommand;
import com.qwerty.nexus.domain.management.organization.dto.response.OrganizationResponseDto;
import com.qwerty.nexus.domain.management.organization.entity.OrganizationEntity;
import com.qwerty.nexus.domain.management.organization.repository.OrganizationRepository;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.response.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository repository;

    /**
     * 단체 정보 수정
     * @param organization
     */
    public Result<OrganizationResponseDto> update(OrganizationUpdateCommand organization) {
        // 업데이트를 하는 사람이 해당 조직의 소속된 사람인지, SUPER 권한을 가졌는지 확인
        // true 면 아래 update 진행
        //AdminResponseDTO admin = adminService.selectOneAdmin(organization.getAdmin().getAdminId());
        //admin.getOrgId();

        OrganizationResponseDto rst = new OrganizationResponseDto();

        OrganizationEntity orgEntity = OrganizationEntity.builder()
                .orgId(organization.getOrgId())
                .orgNm(organization.getOrgNm())
                .orgCd(organization.getOrgCd())
                .updatedBy(organization.getUpdatedBy())
                .build();

        Optional<OrganizationEntity> updateRst = Optional.ofNullable(repository.updateOrganization(orgEntity));
        if(updateRst.isEmpty()){
            Result.Failure.of("단체 정보 수정 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(rst, "단체 정보 수정 성공.");

    }

    /**
     * 한 건의 단체 정보 가져오기
     * @param orgId 단체 아이디 (PK)
     * @return
     */
    public Result<OrganizationResponseDto> selectOneOrganization(int orgId) {
        OrganizationResponseDto rst = new OrganizationResponseDto();

        Optional<OrganizationEntity> selectRst = Optional.ofNullable(repository.selectOneOrganization(orgId));
        if(selectRst.isPresent()){
            rst.convertEntityToDto(selectRst.get());
        }
        else{
            return Result.Failure.of("단체 정보 존재하지 않음.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(rst, "단체 정보 조회 완료.");
    }
}
