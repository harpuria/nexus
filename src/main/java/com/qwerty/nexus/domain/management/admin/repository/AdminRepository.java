package com.qwerty.nexus.domain.management.admin.repository;

import com.qwerty.nexus.domain.management.admin.entity.AdminEntity;
import com.qwerty.nexus.global.paging.entity.PagingEntity;
import lombok.extern.log4j.Log4j2;
import org.jooq.*;
import org.jooq.generated.tables.JAdmin;
import org.jooq.generated.tables.daos.AdminDao;
import org.jooq.generated.tables.records.AdminRecord;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Log4j2
@Repository
public class AdminRepository {
    private final DSLContext dslContext;
    private final JAdmin ADMIN = JAdmin.ADMIN;
    private final AdminDao dao;

    public AdminRepository(Configuration configuration, DSLContext dslContext) {
        this.dslContext = dslContext;
        this.dao = new AdminDao(configuration);
    }

    /**
     * 회원 등록
     * @param admin 등록할 관리자 정보
     * @return Long 관리자 아이디(PK)를 담은 객체 반환
     */
    public Integer insertAdmin(AdminEntity admin){
        AdminRecord record = dslContext.newRecord(ADMIN, admin);
        record.store();

        return record.getAdminId();
    }

    /**
     * 회원 정보 수정
     * @param admin 수정할 관리자 정보
     * @return int 수정된 row 개수
     */
    public int updateAdmin(AdminEntity admin){
        AdminRecord record = dslContext.newRecord(ADMIN, admin);
        record.changed(ADMIN.LOGIN_ID, admin.getLoginId() != null);
        record.changed(ADMIN.LOGIN_PW, admin.getLoginPw() != null);
        record.changed(ADMIN.ADMIN_ROLE, admin.getAdminRole() != null);
        record.changed(ADMIN.ADMIN_NM, admin.getAdminNm() != null);
        record.changed(ADMIN.ADMIN_EMAIL, admin.getAdminEmail() != null);
        record.changed(ADMIN.ORG_ID, admin.getOrgId() != null);
        record.changed(ADMIN.GAME_ID, admin.getGameId() != null);
        record.changed(ADMIN.UPDATED_AT, admin.getUpdatedAt() != null);
        record.changed(ADMIN.UPDATED_BY, admin.getUpdatedBy() != null);
        record.changed(ADMIN.IS_DEL, admin.getIsDel() != null);
        return record.update();
    }

    /**
     * 아이디 중복 체크
     * @param admin
     * @return Integer
     */
    public Integer existsByLoginId(AdminEntity admin){
        return dslContext.selectCount()
                .from(ADMIN)
                .where(ADMIN.LOGIN_ID.eq(admin.getLoginId()))
                .fetchOneInto(Integer.class);
    }

    /**
     * 이메일 중복 체크
     * @param admin
     * @return integer
     */
    public Integer existsByEmail(AdminEntity admin){
        return dslContext.selectCount()
                .from(ADMIN)
                .where(ADMIN.ADMIN_EMAIL.eq(admin.getAdminEmail()))
                .fetchOneInto(Integer.class);
    }

    /**
     * 비밀번호 체크
     * @param admin
     * @return
     */
    public Integer existsByAdminIdAndLoginPw(AdminEntity admin){
        return dslContext.selectCount()
                .from(ADMIN)
                .where(ADMIN.ADMIN_ID.eq(admin.getAdminId())
                        .and(ADMIN.LOGIN_PW.eq(admin.getLoginPw())))
                .fetchOneInto(Integer.class);
    }

    /**
     * 한 건의 회원 정보 조회
     * @param adminId
     * @return
     */
    public AdminEntity findByAdminId(Integer adminId){
        // 조건 설정
        Condition condition = DSL.noCondition();
        condition = condition.and(ADMIN.IS_DEL.isNull().or(ADMIN.IS_DEL.eq("N")));

        if(adminId != null){
            condition = condition.and(ADMIN.ADMIN_ID.eq(adminId));
        }

        return dslContext.selectFrom(ADMIN)
                .where(condition)
                .fetchOneInto(AdminEntity.class);
    }

    /**
     * 관리자 목록 조회
     * @return
     */
    public List<AdminEntity> findAllByKeyword(PagingEntity pagingEntity){
        // 조건 설정
        Condition condition = DSL.noCondition();
        condition = condition.and(ADMIN.IS_DEL.isNull().or(ADMIN.IS_DEL.eq("N")));

        // 키워드 검색 (이름검색 <추후 필요시 검색 조건 나눠서 검색하는 부분 만들것>)
        if (pagingEntity.getKeyword() != null && !pagingEntity.getKeyword().isBlank()) {
            String keyword = "%" + pagingEntity.getKeyword().trim() + "%";
            condition = condition.and(
                    ADMIN.ADMIN_NM.likeIgnoreCase(keyword)
            );
        }

        // 정렬 기준 설정
        String sortDirection = Optional.ofNullable(pagingEntity.getDirection()).orElse("DESC");
        int size = pagingEntity.getSize();
        int page = pagingEntity.getPage();
        int offset = page * size;
        Condition finalCondition = condition;

        return dslContext.selectFrom(ADMIN)
                .where(finalCondition)
                .orderBy(resolveSortField(pagingEntity.getSort(), sortDirection))
                .limit(size)
                .offset(offset)
                .fetchInto(AdminEntity.class);
    }

    /**
     * admin_id(PK) 로 login_id 가져오기
     * @param adminId
     * @return
     */
    public String findLoginIdByAdminId(int adminId){
        return dslContext.select(ADMIN.LOGIN_ID)
                .from(ADMIN)
                .where(ADMIN.ADMIN_ID.eq(adminId))
                .fetchOneInto(String.class);
    }

    /**
     * 로그인 아이디 조회
     * @param loginId
     * @return
     */
    public Optional<AdminEntity> findByLoginId(String loginId) {
        return Optional.ofNullable(dslContext.selectFrom(ADMIN)
                .where(ADMIN.LOGIN_ID.eq(loginId))
                .fetchOneInto(AdminEntity.class));
    }

    /**
     * 삭제되지 않은 전체 관리자 개수 조회
     * @return 전체 건수
     */
    public long countActiveAdmins() {
        Long count = dslContext.selectCount()
                .from(ADMIN)
                .where(ADMIN.IS_DEL.eq("N"))
                .fetchOneInto(Long.class);

        return count != null ? count : 0L;
    }

    /**
     * 정렬 필드 설정
     * @param sort
     * @param direction
     * @return
     */
    private SortField<?> resolveSortField(String sort, String direction) {
        Field<?> sortField;
        String sortKey = Optional.ofNullable(sort).orElse("createdAt").toLowerCase(Locale.ROOT);

        sortField = switch (sortKey) {
            case "adminid" -> ADMIN.ADMIN_ID;
            case "loginid" -> ADMIN.LOGIN_ID;
            case "adminnm" -> ADMIN.ADMIN_NM;
            case "adminemail" -> ADMIN.ADMIN_EMAIL;
            case "adminrole" -> ADMIN.ADMIN_ROLE;
            case "updatedat" -> ADMIN.UPDATED_AT;
            default -> ADMIN.CREATED_AT;
        };

        boolean isAsc = "ASC".equalsIgnoreCase(direction);
        return isAsc ? sortField.asc() : sortField.desc();
    }
}
