package com.qwerty.nexus;

import com.qwerty.nexus.admin.AdminRepository;
import com.qwerty.nexus.admin.AdminRole;
import org.jooq.generated.tables.pojos.Admin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class AdminTests {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insert(){
        Admin admin = new Admin();
        admin.setOrgId(48);
        admin.setLoginId("admin");
        admin.setLoginPw(passwordEncoder.encode("admin"));
        admin.setAdminNm("운영자");
        admin.setAdminEmail("abcd@naver.com");
        admin.setAdminRole(AdminRole.NEXUS.name());
        admin.setCreatedBy("홍길동");
        admin.setUpdatedBy("홍길동");
        adminRepository.insertAdmin(admin);
    }

    @Test
    public void update(){
        Admin admin = new Admin();
        admin.setAdminId(2);
        admin.setAdminNm("변경된이름");
        admin.setLoginPw(passwordEncoder.encode("passwd"));
        adminRepository.updateAdmin(admin);
    }
}
