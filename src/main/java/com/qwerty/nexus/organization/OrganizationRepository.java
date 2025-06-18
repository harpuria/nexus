package com.qwerty.nexus.organization;

import lombok.extern.log4j.Log4j2;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JOrganization;
import org.jooq.generated.tables.daos.OrganizationDao;
import org.jooq.generated.tables.pojos.Organization;
import org.jooq.generated.tables.records.OrganizationRecord;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
    public Organization insertOrganization(Organization organization){
        dao.insert(organization);
        return organization;
    }

    /**
     * 단체 정보 갱신
     * @param organization
     * @return
     */
    public Organization updateOrganization(Organization organization){
        OrganizationRecord record = dslContext.newRecord(ORGANIZATION, organization);
        // 각 필드가 null 값이 아닌 경우(true)에만 변경처리
        record.changed(ORGANIZATION.ORG_ID, organization.getOrgId() != null);
        record.changed(ORGANIZATION.ORG_NM, organization.getOrgNm() != null);
        record.changed(ORGANIZATION.ORG_CD, organization.getOrgCd() != null);
        record.changed(ORGANIZATION.IS_DEL, organization.getIsDel() != null);
        record.update();
        return organization;
    }

    /**
     * 한 건의 단체 정보 가져오기
     * @param id
     * @return
     */
    public Organization selectOneOrganization(Integer id){
        return dslContext.selectFrom(ORGANIZATION)
                .where(ORGANIZATION.ORG_ID.eq(id))
                .fetchOneInto(Organization.class);
    }
}
