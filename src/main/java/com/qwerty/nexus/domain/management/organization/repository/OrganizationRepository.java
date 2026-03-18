package com.qwerty.nexus.domain.management.organization.repository;

import com.qwerty.nexus.domain.management.organization.entity.OrganizationEntity;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.PagingEntity;
import lombok.extern.log4j.Log4j2;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.generated.tables.JOrganization;
import org.jooq.generated.tables.daos.OrganizationDao;
import org.jooq.generated.tables.records.OrganizationRecord;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;
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
     * 단체 생성
     *
     * @param entity 단체 엔티티
     * @return 생성된 단체 PK
     */
    public Integer insertOrganization(OrganizationEntity entity) {
        OrganizationRecord record = dslContext.newRecord(ORGANIZATION, entity);
        record.store();

        return record.getOrgId();
    }

    /**
     * 단체 정보 수정
     *
     * @param entity 단체 엔티티
     * @return 수정된 행 수
     */
    public int updateOrganization(OrganizationEntity entity) {
        OrganizationRecord record = dslContext.newRecord(ORGANIZATION, entity);
        record.changed(ORGANIZATION.ORG_NM, entity.getOrgNm() != null);
        record.changed(ORGANIZATION.ORG_CD, entity.getOrgCd() != null);
        record.changed(ORGANIZATION.IMAGE_PATH, entity.getImagePath() != null);
        record.changed(ORGANIZATION.UPDATED_BY, entity.getUpdatedBy() != null);
        record.changed(ORGANIZATION.IS_DEL, entity.getIsDel() != null);
        return record.update();
    }

    /**
     * 단체 삭제 (논리 삭제)
     *
     * @param entity 단체 삭제 정보
     * @return 수정된 행 수
     */
    public int deleteOrganization(OrganizationEntity entity) {
        OrganizationRecord record = dslContext.newRecord(ORGANIZATION, entity);
        record.changed(ORGANIZATION.UPDATED_BY, entity.getUpdatedBy() != null);
        record.changed(ORGANIZATION.IS_DEL, entity.getIsDel() != null);
        return record.update();
    }

    /**
     * 단체 단건 조회
     *
     * @param orgId 단체 PK
     * @return 단체 엔티티
     */
    public OrganizationEntity findByOrgId(Integer orgId) {
        if (orgId == null) {
            return null;
        }

        Condition condition = ORGANIZATION.IS_DEL.eq("N")
                .and(ORGANIZATION.ORG_ID.eq(orgId));

        return dslContext.selectFrom(ORGANIZATION)
                .where(condition)
                .fetchOneInto(OrganizationEntity.class);
    }

    /**
     * 단체 목록 조회
     *
     * @param pagingEntity 페이징 정보
     * @return 단체 목록
     */
    public List<OrganizationEntity> findAllByPaging(PagingEntity pagingEntity) {
        Condition condition = ORGANIZATION.IS_DEL.eq("N");

        int size = pagingEntity.getSize();
        int page = pagingEntity.getPage();
        int offset = page * size;
        String sortDirection = Optional.ofNullable(pagingEntity.getDirection())
                .orElse(ApiConstants.Pagination.DEFAULT_SORT_DIRECTION);

        return dslContext.selectFrom(ORGANIZATION)
                .where(condition)
                .orderBy(resolveSortField(pagingEntity.getSort(), sortDirection))
                .limit(size)
                .offset(offset)
                .fetchInto(OrganizationEntity.class);
    }

    /**
     * 삭제되지 않은 단체 수 조회
     *
     * @return 단체 수
     */
    public long countActiveOrganizations() {
        Long count = dslContext.selectCount()
                .from(ORGANIZATION)
                .where(ORGANIZATION.IS_DEL.eq("N"))
                .fetchOneInto(Long.class);

        return count != null ? count : 0L;
    }

    /**
     * 정렬 필드 설정
     *
     * @param sort 정렬 키
     * @param direction 정렬 방향
     * @return 정렬 필드
     */
    private SortField<?> resolveSortField(String sort, String direction) {
        Field<?> sortField;
        String sortKey = Optional.ofNullable(sort)
                .orElse(ApiConstants.Pagination.DEFAULT_SORT_FIELD)
                .toLowerCase(Locale.ROOT);

        sortField = switch (sortKey) {
            case "orgid" -> ORGANIZATION.ORG_ID;
            case "orgnm" -> ORGANIZATION.ORG_NM;
            case "orgcd" -> ORGANIZATION.ORG_CD;
            case "updatedat" -> ORGANIZATION.UPDATED_AT;
            default -> ORGANIZATION.CREATED_AT;
        };

        boolean isAsc = ApiConstants.Pagination.SORT_ASC.equalsIgnoreCase(direction);
        return isAsc ? sortField.asc() : sortField.desc();
    }
}

