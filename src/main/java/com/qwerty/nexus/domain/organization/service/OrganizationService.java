package com.qwerty.nexus.domain.organization.service;

import com.qwerty.nexus.domain.organization.command.OrganizationCreateCommand;
import com.qwerty.nexus.domain.organization.command.OrganizationUpdateCommand;
import com.qwerty.nexus.domain.organization.dto.response.OrganizationResponseDTO;
import com.qwerty.nexus.domain.organization.entity.OrganizationEntity;
import com.qwerty.nexus.domain.organization.repository.OrganizationRepository;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.response.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jooq.generated.tables.records.OrganizationRecord;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    /**
     *
     * @param organization
     */
    public Result<OrganizationResponseDTO> update(OrganizationUpdateCommand organization) {
        // 업데이트를 하는 사람이 해당 조직의 소속된 사람인지, SUPER 권한을 가졌는지 확인
        // true 면 아래 update 진행
        //AdminResponseDTO admin = adminService.selectOneAdmin(organization.getAdmin().getAdminId());
        //admin.getOrgId();

        OrganizationResponseDTO rst = new OrganizationResponseDTO();

        OrganizationRecord record = new OrganizationRecord();
        record.setOrgNm(organization.getOrgNm());
        record.setOrgCd(organization.getOrgCd());
        record.setUpdatedBy(organization.getUpdatedBy());

        Optional<OrganizationRecord> updateRst = Optional.ofNullable(organizationRepository.updateOrganization(record));
        if(updateRst.isPresent()){
            rst.setMessage("단체 정보 수정에 성공하였습니다.");
        }
        else{
            Result.Failure.of("단체 정보 수정에 실패하였습니다. 넥서스 관리자에게 문의해주세요.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(rst);

    }

    /**
     * 단체 정보 생성
     * @param organization
     */
    public Result<OrganizationResponseDTO> register(OrganizationCreateCommand organization) {
        OrganizationResponseDTO rst = new OrganizationResponseDTO();

        OrganizationEntity entity = OrganizationEntity.builder()
                .orgNm(organization.getOrgNm())
                .orgCd(organization.getOrgCd())
                .createdBy(organization.getCreateBy())
                .updatedBy(organization.getCreateBy())
                .build();

        Optional<OrganizationEntity> insertRst = Optional.ofNullable(organizationRepository.insertOrganization(entity));
        if(insertRst.isPresent()){
            rst.setMessage("단체 정보가 정상적으로 생성되었습니다.");
        }else{
            return Result.Failure.of("단체 정보 생성에 실패하였습니다. 넥서스 관리자에게 문의해주세요.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(rst);
    }
}
