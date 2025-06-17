package com.qwerty.nexus;

import com.qwerty.nexus.admin.AdminRepository;
import com.qwerty.nexus.admin.AdminRole;
import com.qwerty.nexus.organization.OrganizationRepository;
import org.jooq.generated.tables.pojos.Admin;
import org.jooq.generated.tables.pojos.Organization;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrganizationTests {
    @Autowired
    private OrganizationRepository repository;

    @Test
    public void insert(){

    }

    @Test
    void jooqInsertTest_Record(){
        repository.testRecordInsert();
    }

    @Test
    void jooqUpdateTest_Record() {
        repository.testRecordUpdate();
    }

    @Test
    void jooqSelectTest_DAO(){
        repository.testDaoSelect();
    }
}
