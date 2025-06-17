package com.qwerty.nexus.admin;

import lombok.extern.log4j.Log4j2;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JAdmin;
import org.jooq.generated.tables.daos.AdminDao;
import org.jooq.generated.tables.pojos.Admin;
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
    public Admin insertAdmin(Admin admin){
        AdminRecord record = dslContext.newRecord(ADMIN);

        // dao 하면 오류가 나네??? record 로 바꿔야겄다.
        dao.insert(admin);
        return admin;
    }

    /**
     * 회원 정보 갱신
     * @param admin
     * @return admin
     */
    public Admin updateAdmin(Admin admin){
        dao.update(admin);
        return admin;
    }

    /**
     * 아이디 중복 체크
     * @param admin
     * @return integer
     */
    public Integer isUserAlreadyRegistered(Admin admin){
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
    public Integer existsByEmail(Admin admin){
        return dslContext.selectCount()
                .from(ADMIN)
                .where(ADMIN.ADMIN_EMAIL.eq(admin.getAdminEmail()))
                .fetchOneInto(Integer.class);
    }

    /**
     * 한 건의 회원 정보 조회
     * @param admin
     * @return
     */
    public Admin selectOneAdmin(Admin admin){
        // 조건 설정
        Condition condition = DSL.noCondition();
        if(!admin.getLoginId().isEmpty()){
            condition.and(ADMIN.LOGIN_ID.eq(admin.getLoginId()));
        }

        if(admin.getAdminId() != null){
            condition.and(ADMIN.ADMIN_ID.eq(admin.getAdminId()));
        }

        return dslContext.selectFrom(ADMIN)
                .where(condition)
                .fetchOneInto(Admin.class);
    }

    /**
     * 전체 회원 정보 조회 (추후 페이징, 검색 조건 처리 등 필요)
     * @return
     */
    public List<Admin> selectListAdmin(){
        return dslContext.selectFrom(ADMIN)
                .fetchInto(Admin.class);
    }
}
