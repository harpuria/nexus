package com.qwerty.nexus.domain.management.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qwerty.nexus.domain.management.admin.AdminRole;
import com.qwerty.nexus.domain.management.admin.entity.AdminEntity;
import com.qwerty.nexus.domain.management.admin.repository.AdminRepository;
import com.qwerty.nexus.domain.management.admin.service.AdminService;
import com.qwerty.nexus.domain.management.admin.service.AdminTokenBlacklist;
import com.qwerty.nexus.domain.management.organization.repository.OrganizationRepository;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.filter.JwtAuthenticationFilter;
import com.qwerty.nexus.global.util.jwt.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(AdminService.class)
class AdminControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AdminRepository adminRepository;

    @MockitoBean
    private OrganizationRepository organizationRepository;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private AdminTokenBlacklist adminTokenBlacklist;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void createInitialAdmin_success() throws Exception {
        given(adminRepository.existsByLoginId(any())).willReturn(0);
        given(adminRepository.existsByEmail(any())).willReturn(0);
        given(organizationRepository.insertOrganization(any())).willReturn(10);
        given(adminRepository.insertAdmin(any())).willReturn(20);

        String body = objectMapper.writeValueAsString(Map.of(
                "loginId", "superadmin",
                "loginPw", "password123!",
                "adminEmail", "super@nexus.io",
                "adminNm", "슈퍼관리자",
                "orgNm", "넥서스",
                "orgCd", "123-45-67890"
        ));

        mockMvc.perform(post(ApiConstants.Path.ADMIN_PATH + "/initialize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("회원가입 완료."));
    }

    @Test
    void getAdmin_success() throws Exception {
        given(adminRepository.findByAdminId(20)).willReturn(AdminEntity.builder()
                .adminId(20)
                .orgId(10)
                .loginId("superadmin")
                .adminRole(AdminRole.SUPER)
                .adminEmail("super@nexus.io")
                .adminNm("슈퍼관리자")
                .isDel("N")
                .build());

        mockMvc.perform(get(ApiConstants.Path.ADMIN_PATH + "/{adminId}", 20))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.adminId").value(20))
                .andExpect(jsonPath("$.data.loginId").value("superadmin"));
    }

    @Test
    void loginAdmin_notFound() throws Exception {
        given(adminRepository.findByLoginId("unknown")).willReturn(Optional.empty());

        String body = objectMapper.writeValueAsString(Map.of(
                "loginId", "unknown",
                "loginPw", "password"
        ));

        mockMvc.perform(post(ApiConstants.Path.ADMIN_PATH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("관리자 계정이 존재하지 않습니다."));
    }
}
