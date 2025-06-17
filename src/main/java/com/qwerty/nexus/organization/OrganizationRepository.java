package com.qwerty.nexus.organization;

import lombok.extern.log4j.Log4j2;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JOrganization;
import org.jooq.generated.tables.daos.OrganizationDao;
import org.jooq.generated.tables.pojos.Organization;
import org.jooq.generated.tables.records.OrganizationRecord;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
public class OrganizationRepository {
    private final DSLContext dslContext;
    private final JOrganization ORGANIZATION = JOrganization.ORGANIZATION;
    private final OrganizationDao dao;

    public OrganizationRepository(Configuration configuration, DSLContext dslContext) {
        this.dslContext = dslContext;
        this.dao = new OrganizationDao(configuration);
    }

    public void testDaoInsert(Organization organization) {
        dao.insert(organization);
    }

    public void testRecordInsert (){
        OrganizationRecord record = dslContext.newRecord(ORGANIZATION);
        record.setOrgNm("그리즐리소프트");
        record.setOrgCd("123-45-67890");
        record.setCreatedBy("test");
        record.setUpdatedBy("test");
        record.store();
        log.info("testRecordInsert orgId = {}", record.getOrgId());
    }

    public void testRecordUpdate() {
        OrganizationRecord record = dslContext.newRecord(ORGANIZATION);
        record.setOrgId(44);
        record.setOrgNm("구리구리소프트");
        record.update();
    }

    public void testDaoSelect() {
        Organization org = dao.findById(43);
        log.info("testDaoSelect org = {}", org);
    }
}
