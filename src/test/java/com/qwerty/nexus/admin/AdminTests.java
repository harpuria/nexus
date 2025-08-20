package com.qwerty.nexus.admin;

import com.qwerty.nexus.domain.management.admin.service.AdminService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class AdminTests {
    @Autowired
    private AdminService adminService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("SUPER 관리자 등록 케이스")
    public void registerAdmin(){

    }

    @Test
    @DisplayName("관리자 수정 케이스")
    public void updateAdmin(){

    }

    @Test
    @DisplayName("한 명의 관리자 조회 케이스")
    public void selectOneAdmin(){
        int adminId = 1; // 상황에 따라서 수정
        System.out.println(adminService.selectOneAdmin(adminId));
    }
}
