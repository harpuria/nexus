package com.qwerty.nexus.admin;

import com.qwerty.nexus.domain.game.user.service.GameUserService;
import com.qwerty.nexus.domain.management.admin.command.AdminCreateCommand;
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
    private GameUserService gameUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("관리자 등록 서비스 테스트")
    public void registerAdmin(){

        // 관리자 여러명 등록
        for(int i = 0; i < 20; i++){
            AdminCreateCommand command = AdminCreateCommand.builder()
                    .loginId(String.format("admin%s", i + 1))
                    .loginPw("admin")
                    .adminEmail(String.format("admin%s@admin.com", i + 1))
                    .adminNm(String.format("adminName%s", i + 1))
                    .adminRole("SUPER")
                    .orgId(1)
                    .build();

            adminService.register(command);
        }
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

    @Test
    public void jwtTest(){
        gameUserService.authenticate(null);
    }
}
