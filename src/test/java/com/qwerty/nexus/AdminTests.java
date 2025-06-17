package com.qwerty.nexus;

import com.qwerty.nexus.admin.AdminRepository;
import com.qwerty.nexus.admin.AdminRole;
import org.jooq.generated.tables.pojos.Admin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdminTests {
    @Autowired
    private AdminRepository adminRepository;

    @Test
    public void insert(){
        Admin admin = new Admin();
        admin.setOrgId(2);
        admin.setLoginId("admin");
        admin.setLoginPw("admin");
        admin.setAdminNm("운영자");
        admin.setAdminEmail("abcd@naver.com");
        admin.setAdminRole(AdminRole.NEXUS.name());
        admin.setCreatedBy("홍길동");
        admin.setUpdatedBy("홍길동");
        adminRepository.insertAdmin(admin);
    }
}
