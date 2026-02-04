package com.qwerty.nexus.domain.management.organization.service;

import com.qwerty.nexus.domain.management.organization.dto.request.OrganizationUpdateRequestDto;
import com.qwerty.nexus.domain.management.organization.dto.response.OrganizationResponseDto;
import com.qwerty.nexus.domain.management.organization.entity.OrganizationEntity;
import com.qwerty.nexus.domain.management.organization.repository.OrganizationRepository;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.response.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository repository;

    /**
     * 단체 정보 수정
     * @param dto
     */
    @Transactional
    public Result<Void> updateOrganization(OrganizationUpdateRequestDto dto) {
        // 업데이트를 하는 사람이 해당 조직의 소속된 사람인지, SUPER 권한을 가졌는지 확인
        // true 면 아래 update 진행
        //AdminResponseDTO admin = adminService.selectOneAdmin(organization.getAdmin().getAdminId());
        //admin.getOrgId();
        OrganizationEntity orgEntity = OrganizationEntity.builder()
                .orgId(dto.getOrgId())
                .orgNm(dto.getOrgNm())
                .orgCd(dto.getOrgCd())
                .updatedBy(dto.getUpdatedBy())
                .build();

        int updatedRows = repository.updateOrganization(orgEntity);
        if(updatedRows > 0){
            return Result.Success.of(null, "단체 정보 수정 성공.");
        }
        else{
            return Result.Failure.of("단체 정보 수정 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }
    }

    /**
     * 한 건의 단체 정보 가져오기
     * @param orgId 단체 아이디 (PK)
     * @return
     */
    public Result<OrganizationResponseDto> getOrganization(int orgId) {
        Optional<OrganizationEntity> selectRst = Optional.ofNullable(repository.findByOrgId(orgId));
        if(selectRst.isPresent()){
            return Result.Success.of(OrganizationResponseDto.from(selectRst.get()), "단체 정보 조회 완료.");
        }
        else{
            return Result.Failure.of("단체 정보 존재하지 않음.", ErrorCode.NOT_FOUND.getCode());
        }
    }
}
