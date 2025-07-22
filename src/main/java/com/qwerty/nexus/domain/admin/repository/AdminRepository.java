package com.qwerty.nexus.domain.admin.repository;

import com.qwerty.nexus.domain.admin.entity.AdminEntity;
import lombok.extern.log4j.Log4j2;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JAdmin;
import org.jooq.generated.tables.daos.AdminDao;
import org.jooq.generated.tables.records.AdminRecord;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;

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
     * @param admin
     * @return admin
     */
    public AdminEntity insertAdmin(AdminEntity admin){
        AdminRecord record = dslContext.newRecord(ADMIN, admin);
        record.store();

        return AdminEntity.builder()
                .adminId(record.getAdminId())
                .build();
    }

    /**
     * 회원 정보 수정
     * @param admin
     * @return admin
     */
    public AdminEntity updateAdmin(AdminEntity admin){
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
        record.update();
        return admin;
    }

    /**
     * 아이디 중복 체크
     * @param admin
     * @return integer
     */
    public Integer isUserAlreadyRegistered(AdminEntity admin){
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
    public Integer checkPassword(AdminEntity admin){
        return dslContext.selectCount()
                .from(ADMIN)
                .where(ADMIN.ADMIN_ID.eq(admin.getAdminId())
                        .and(ADMIN.LOGIN_PW.eq(admin.getLoginPw())))
                .fetchOneInto(Integer.class);
    }

    /**
     * 한 건의 회원 정보 조회
     * @param admin
     * @return
     */
    public AdminEntity selectOneAdmin(AdminEntity admin){
        // 조건 설정
        Condition condition = DSL.noCondition();
        if(admin.getLoginId() != null && !admin.getLoginId().isEmpty()){
            condition = condition.and(ADMIN.LOGIN_ID.eq(admin.getLoginId()));
        }

        if(admin.getAdminId() != null){
            condition = condition.and(ADMIN.ADMIN_ID.eq(admin.getAdminId()));
        }

        return dslContext.selectFrom(ADMIN)
                .where(condition)
                .fetchOneInto(AdminEntity.class);
    }

    /**
     * 전체 회원 정보 조회 (추후 페이징, 검색 조건 처리 등 필요)
     * @return
     */
    public List<AdminEntity> selectListAdmin(){
        return dslContext.selectFrom(ADMIN)
                .fetchInto(AdminEntity.class);
    }


    /**
     * admin_id(PK) 로 login_id 가져오기
     * @param adminId
     * @return
     */
    public String selectOneLoginId(int adminId){
        return dslContext.select(ADMIN.LOGIN_ID)
                .from(ADMIN)
                .where(ADMIN.ADMIN_ID.eq(adminId))
                .fetchOneInto(String.class);
    }
}
