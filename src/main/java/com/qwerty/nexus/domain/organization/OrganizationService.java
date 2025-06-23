package com.qwerty.nexus.domain.organization;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jooq.generated.tables.records.OrganizationRecord;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    /**
     *
     * @param organization
     */
    public void update(OrganizationRequestDTO organization) {
        // 업데이트를 하는 사람이 해당 조직의 소속된 사람인지, SUPER 권한을 가졌는지 확인
        // true 면 아래 update 진행
        //AdminResponseDTO admin = adminService.selectOneAdmin(organization.getAdmin().getAdminId());
        //admin.getOrgId();

        organizationRepository.updateOrganization(organization.toAdminRecord());
    }

    /**
     *
     * @param organization
     */
    public int register(OrganizationRequestDTO organization) {
        return organizationRepository.insertOrganization(organization.toAdminRecord());
    }
}
