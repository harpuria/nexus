package com.qwerty.nexus.domain.management.organization.repository;

import com.qwerty.nexus.domain.management.organization.entity.OrganizationEntity;
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
     * @param entity
     * @return Organization
     */
    public OrganizationEntity insertOrganization(OrganizationEntity entity){
        OrganizationRecord record = dslContext.newRecord(ORGANIZATION, entity);
        record.store();

        return OrganizationEntity.builder()
                .orgId(record.getOrgId())
                .build();
    }

    /**
     * 단체 정보 수정
     * @param entity
     * @return
     */
    public OrganizationEntity updateOrganization(OrganizationEntity entity){
        OrganizationRecord record = dslContext.newRecord(ORGANIZATION, entity);
        record.changed(ORGANIZATION.ORG_NM, entity.getOrgNm() != null);
        record.changed(ORGANIZATION.ORG_CD, entity.getOrgCd() != null);
        record.changed(ORGANIZATION.CREATED_BY, entity.getCreatedBy() != null);
        record.changed(ORGANIZATION.UPDATED_BY, entity.getUpdatedBy() != null);
        record.changed(ORGANIZATION.IS_DEL, entity.getIsDel() != null);
        record.update();
        return entity;
    }

    /**
     * 한 건의 단체 정보 가져오기
     * @param orgId
     * @return
     */
    public OrganizationEntity findByOrgId(Integer orgId){
        return dslContext.selectFrom(ORGANIZATION)
                .where(ORGANIZATION.ORG_ID.eq(orgId))
                .fetchOneInto(OrganizationEntity.class);
    }
}
