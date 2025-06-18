package com.qwerty.nexus.organization;

import com.qwerty.nexus.admin.AdminResponseDTO;
import com.qwerty.nexus.admin.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jooq.generated.tables.pojos.Organization;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final AdminService adminService;
    private final OrganizationRepository organizationRepository;

    /**
     *
     * @param organization
     */
    public void update(OrganizationRequestDTO organization) {
        // 업데이트를 하는 사람이 해당 조직의 소속된 사람인지, SUPER 권한을 가졌는지 확인
        // true 면 아래 update 진행
        AdminResponseDTO admin = adminService.selectOneAdmin(organization.getAdmin().getAdminId());
        admin.getOrgId();

        organizationRepository.updateOrganization(organization);
    }

    /**
     *
     * @param organization
     */
    public void register(Organization organization) {
        organizationRepository.insertOrganization(organization);
    }
}
