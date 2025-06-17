package com.qwerty.nexus.admin;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
public class AdminRepository {
    /* 이전에 쓰던거 가져옴

     private final DSLContext dslContext;
    private final JAdmin ADMIN = JAdmin.ADMIN;
    private final AdminDao dao;

    public AdminRepository(Configuration configuration, DSLContext dslContext) {
        this.dslContext = dslContext;
        this.dao = new AdminDao(configuration);
    }

    // id 중복 체크
    public Integer isUserAlreadyRegistered(Admin admin){
        return dslContext.selectCount()
                .from(ADMIN)
                .where(ADMIN.LOGIN_ID.eq(admin.getLoginId()))
                .fetchOneInto(Integer.class);
    }

    // email 중복 체크
    public Integer existsByEmail(Admin admin){
        return dslContext.selectCount()
                .from(ADMIN)
                .where(ADMIN.ADMIN_EMAIL.eq(admin.getAdminEmail()))
                .fetchOneInto(Integer.class);
    }

    // 회원등록
    public Admin insertAdmin(Admin admin) {
        dao.insert(admin);
        return admin;
    }

    // 회원수정
    public Admin updateAdmin(Admin admin) {
        dao.update(admin);
        return admin;
    }

    // 회원 정보 한건 가져오기
    public Admin selectOneAdmin(Admin admin) {
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

    // 회원 정보 전체 가져오기 (상황에 따라서 페이징, 검색조건 처리도 필요함)
    public List<Admin> selectListAdmin(){
        return dslContext.selectFrom(ADMIN)
                .fetchInto(Admin.class);
    }

     */
}
