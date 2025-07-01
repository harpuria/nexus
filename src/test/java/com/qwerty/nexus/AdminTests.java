package com.qwerty.nexus;

import com.qwerty.nexus.domain.admin.dto.request.AdminRequestDto;
import com.qwerty.nexus.domain.admin.AdminRole;
import com.qwerty.nexus.domain.admin.service.AdminService;
import org.jooq.generated.tables.pojos.Organization;
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
        AdminRequestDto adminRequestDTO = new AdminRequestDto();
        adminRequestDTO.setLoginId("admin2");
        adminRequestDTO.setLoginPw(passwordEncoder.encode("admin"));
        adminRequestDTO.setAdminNm("윤홍훈");
        adminRequestDTO.setAdminEmail("grizzly2@naver.com");
        adminRequestDTO.setAdminRole(AdminRole.SUPER.name());
        adminRequestDTO.setCreatedBy("admin2");
        adminRequestDTO.setUpdatedBy("admin2");

        // SUPER 인 경우 단체 정보 기입
        Organization organization = new Organization();
        organization.setOrgNm("그리즐리소프트2");
        organization.setOrgCd("123-456-789123");
        organization.setCreatedBy("admin");
        organization.setUpdatedBy("admin");

        adminRequestDTO.setOrganization(organization);

        adminService.register(adminRequestDTO);
    }

    @Test
    @DisplayName("관리자 수정 케이스")
    public void updateAdmin(){
        AdminRequestDto adminRequestDTO = new AdminRequestDto();
        adminRequestDTO.setAdminId(1); // 상황에 따라서 수정
        adminRequestDTO.setAdminNm("홍길동");
        adminRequestDTO.setIsApprove("Y");

        adminService.update(adminRequestDTO);
    }

    @Test
    @DisplayName("한 명의 관리자 조회 케이스")
    public void selectOneAdmin(){
        int adminId = 1; // 상황에 따라서 수정
        System.out.println(adminService.selectOneAdmin(adminId));
    }
}
