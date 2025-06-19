package com.qwerty.nexus;

import com.qwerty.nexus.admin.AdminRepository;
import com.qwerty.nexus.admin.AdminRole;
import com.qwerty.nexus.organization.OrganizationRepository;
import org.jooq.generated.tables.pojos.Admin;
import org.jooq.generated.tables.pojos.Organization;
import org.jooq.generated.tables.records.OrganizationRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrganizationTests {
    @Autowired
    private OrganizationRepository repository;

    @Test
    void insert(){
        OrganizationRecord organization = new OrganizationRecord();
        organization.setOrgNm("그리즐리소프트111");
        organization.setOrgCd("123-123-123456");
        organization.setCreatedBy("test");
        organization.setUpdatedBy("test");
        repository.insertOrganization(organization);
    }

    @Test
    void update(){
        OrganizationRecord organization = new OrganizationRecord();
        organization.setOrgId(48);
        organization.setOrgNm("피플인소프트111");
        repository.updateOrganization(organization);
    }

    @Test
    void selectOne(){
        System.out.println(repository.selectOneOrganization(51));
    }
}
