package com.qwerty.nexus;

import com.qwerty.nexus.domain.organization.OrganizationRepository;
import com.qwerty.nexus.domain.organization.OrganizationRequestDTO;
import com.qwerty.nexus.domain.organization.OrganizationService;
import org.jooq.generated.tables.records.OrganizationRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrganizationTests {

    @Autowired
    private OrganizationService organizationService;

    @Test
    @DisplayName("단체 정보 생성 <일반적으로 단독 생성할 일은 없음>")
    public void createOrganization(){
        OrganizationRecord record = new OrganizationRecord();
        record.setOrgNm("정말구린소프트");
        record.setOrgCd("123-456-789123");
        record.setCreatedBy("admin");
        record.setUpdatedBy("admin");

        organizationService.register(record);
    }

    @Test
    @DisplayName("단체 정보 수정")
    public void updateOrganization(){
        OrganizationRequestDTO organizationRequestDTO = new OrganizationRequestDTO();
        organizationRequestDTO.setOrgId(2); // 상황에 따라 수정
        organizationRequestDTO.setOrgNm("구리구리소프트");
        organizationRequestDTO.setOrgCd("987-765-43210");

        organizationService.update(organizationRequestDTO);
    }
}
