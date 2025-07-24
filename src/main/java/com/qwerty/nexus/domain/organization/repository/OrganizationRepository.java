package com.qwerty.nexus.domain.organization.repository;

import com.qwerty.nexus.domain.organization.entity.OrganizationEntity;
import lombok.extern.log4j.Log4j2;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JOrganization;
import org.jooq.generated.tables.daos.OrganizationDao;
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

    /**
     * 단체 정보 생성
     * @param organization
     * @return Organization
     */
    public OrganizationEntity insertOrganization(OrganizationEntity organization){
        OrganizationRecord record = dslContext.newRecord(ORGANIZATION, organization);
        record.store();

        return OrganizationEntity.builder()
                .orgId(record.getOrgId())
                .build();
    }

    /**
     * 단체 정보 수정
     * @param organization
     * @return
     */
    public OrganizationEntity updateOrganization(OrganizationEntity organization){
        OrganizationRecord record = dslContext.newRecord(ORGANIZATION, organization);
        record.changed(ORGANIZATION.ORG_NM, organization.getOrgNm() != null);
        record.changed(ORGANIZATION.ORG_CD, organization.getOrgCd() != null);
        record.changed(ORGANIZATION.CREATED_BY, organization.getCreatedBy() != null);
        record.changed(ORGANIZATION.UPDATED_BY, organization.getUpdatedBy() != null);
        record.changed(ORGANIZATION.IS_DEL, organization.getIsDel() != null);
        record.update();
        return organization;
    }

    /**
     * 한 건의 단체 정보 가져오기
     * @param orgId
     * @return
     */
    public OrganizationEntity selectOneOrganization(Integer orgId){
        return dslContext.selectFrom(ORGANIZATION)
                .where(ORGANIZATION.ORG_ID.eq(orgId))
                .fetchOneInto(OrganizationEntity.class);
    }
}
