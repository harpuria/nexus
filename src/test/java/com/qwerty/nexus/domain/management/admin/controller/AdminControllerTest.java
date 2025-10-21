package com.qwerty.nexus.domain.management.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qwerty.nexus.domain.management.admin.AdminRole;
import com.qwerty.nexus.domain.management.admin.dto.request.AdminCreateRequestDto;
import com.qwerty.nexus.domain.management.admin.dto.request.AdminInitCreateRequestDto;
import com.qwerty.nexus.domain.management.admin.dto.request.AdminLoginRequestDto;
import com.qwerty.nexus.domain.management.admin.dto.request.AdminSearchRequestDto;
import com.qwerty.nexus.domain.management.admin.dto.request.AdminUpdateRequestDto;
import com.qwerty.nexus.domain.management.admin.dto.response.AdminResponseDto;
import com.qwerty.nexus.domain.management.admin.service.AdminService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.response.Result;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AdminService adminService;

    @Test
    @DisplayName("초기 관리자 생성 성공")
    void initializeAdmin_success() throws Exception {
        AdminInitCreateRequestDto request = new AdminInitCreateRequestDto();
        request.setLoginId("admin");
        request.setLoginPw("password");
        request.setAdminEmail("admin@example.com");
        request.setAdminNm("관리자");
        request.setOrgNm("Org");
        request.setOrgCd("123");

        when(adminService.initialize(any())).thenReturn(Result.Success.of(null, "created"));

        mockMvc.perform(post(ApiConstants.Path.ADMIN_PATH + "/initialize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("created"));

        verify(adminService).initialize(any());
    }

    @Test
    @DisplayName("초기 관리자 생성 실패")
    void initializeAdmin_failure() throws Exception {
        AdminInitCreateRequestDto request = new AdminInitCreateRequestDto();
        request.setLoginId("admin");

        when(adminService.initialize(any())).thenReturn(Result.Failure.of("에러", "ERROR_CODE"));

        mockMvc.perform(post(ApiConstants.Path.ADMIN_PATH + "/initialize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }

    @Test
    @DisplayName("관리자 생성 성공")
    void createAdmin_success() throws Exception {
        AdminCreateRequestDto request = new AdminCreateRequestDto();
        request.setLoginId("admin2");
        request.setLoginPw("password");
        request.setAdminEmail("admin2@example.com");
        request.setAdminNm("관리자2");
        request.setAdminRole(AdminRole.ADMIN);
        request.setOrgId(1);

        when(adminService.create(any())).thenReturn(Result.Success.of(null, "created"));

        mockMvc.perform(post(ApiConstants.Path.ADMIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("created"));
    }

    @Test
    @DisplayName("관리자 생성 실패")
    void createAdmin_failure() throws Exception {
        AdminCreateRequestDto request = new AdminCreateRequestDto();
        request.setLoginId("admin2");

        when(adminService.create(any())).thenReturn(Result.Failure.of("에러", "ERROR_CODE"));

        mockMvc.perform(post(ApiConstants.Path.ADMIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }

    @Test
    @DisplayName("관리자 수정 성공")
    void updateAdmin_success() throws Exception {
        AdminUpdateRequestDto request = new AdminUpdateRequestDto();
        request.setUpdatedBy("tester");

        when(adminService.update(any())).thenReturn(Result.Success.of(null, "updated"));

        mockMvc.perform(patch(ApiConstants.Path.ADMIN_PATH + "/{adminId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("updated"));

        verify(adminService).update(any());
    }

    @Test
    @DisplayName("관리자 수정 실패")
    void updateAdmin_failure() throws Exception {
        AdminUpdateRequestDto request = new AdminUpdateRequestDto();

        when(adminService.update(any())).thenReturn(Result.Failure.of("에러", "ERROR_CODE"));

        mockMvc.perform(patch(ApiConstants.Path.ADMIN_PATH + "/{adminId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }

    @Test
    @DisplayName("관리자 삭제 성공")
    void deleteAdmin_success() throws Exception {
        when(adminService.update(any())).thenReturn(Result.Success.of(null, "deleted"));

        mockMvc.perform(delete(ApiConstants.Path.ADMIN_PATH + "/{adminId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("deleted"));
    }

    @Test
    @DisplayName("관리자 삭제 실패")
    void deleteAdmin_failure() throws Exception {
        when(adminService.update(any())).thenReturn(Result.Failure.of("에러", "ERROR_CODE"));

        mockMvc.perform(delete(ApiConstants.Path.ADMIN_PATH + "/{adminId}", 1))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }

    @Test
    @DisplayName("관리자 단건 조회 성공")
    void selectOneAdmin_success() throws Exception {
        AdminResponseDto response = AdminResponseDto.builder()
                .adminId(1)
                .loginId("admin")
                .build();

        when(adminService.selectOne(1)).thenReturn(Result.Success.of(response, "조회"));

        mockMvc.perform(get(ApiConstants.Path.ADMIN_PATH + "/{adminId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.loginId").value("admin"));
    }

    @Test
    @DisplayName("관리자 단건 조회 실패")
    void selectOneAdmin_failure() throws Exception {
        when(adminService.selectOne(1)).thenReturn(Result.Failure.of("에러", "ERROR_CODE"));

        mockMvc.perform(get(ApiConstants.Path.ADMIN_PATH + "/{adminId}", 1))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }

    @Test
    @DisplayName("관리자 목록 조회 성공")
    void selectAllAdmin_success() throws Exception {
        AdminResponseDto admin = AdminResponseDto.builder()
                .adminId(1)
                .loginId("admin")
                .build();

        when(adminService.selectAll(any())).thenReturn(Result.Success.of(List.of(admin), "조회"));

        mockMvc.perform(get(ApiConstants.Path.ADMIN_PATH + "/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AdminSearchRequestDto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].loginId").value("admin"));
    }

    @Test
    @DisplayName("관리자 목록 조회 실패")
    void selectAllAdmin_failure() throws Exception {
        when(adminService.selectAll(any())).thenReturn(Result.Failure.of("에러", "ERROR_CODE"));

        mockMvc.perform(get(ApiConstants.Path.ADMIN_PATH + "/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AdminSearchRequestDto())))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }

    @Test
    @DisplayName("관리자 로그인 성공")
    void login_success() throws Exception {
        AdminResponseDto response = AdminResponseDto.builder()
                .adminId(1)
                .loginId("admin")
                .build();

        when(adminService.login(any())).thenReturn(Result.Success.of(response, "로그인"));

        mockMvc.perform(post(ApiConstants.Path.ADMIN_PATH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AdminLoginRequestDto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.loginId").value("admin"));
    }

    @Test
    @DisplayName("관리자 로그인 실패")
    void login_failure() throws Exception {
        when(adminService.login(any())).thenReturn(Result.Failure.of("에러", "ERROR_CODE"));

        mockMvc.perform(post(ApiConstants.Path.ADMIN_PATH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AdminLoginRequestDto())))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }
}
