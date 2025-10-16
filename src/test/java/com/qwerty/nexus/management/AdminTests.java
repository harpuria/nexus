package com.qwerty.nexus.management;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qwerty.nexus.domain.management.admin.AdminRole;
import com.qwerty.nexus.domain.management.admin.dto.request.AdminCreateRequestDto;
import com.qwerty.nexus.domain.management.admin.dto.request.AdminInitCreateRequestDto;
import com.qwerty.nexus.domain.management.admin.dto.request.AdminUpdateRequestDto;
import com.qwerty.nexus.global.constant.ApiConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@Transactional // 트랜잭션 롤백용도
public class AdminTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("초기 사용자 등록 테스트")
    public void initRegisterAdmin() throws Exception {
        // given (set parameter)
        AdminInitCreateRequestDto dto = new AdminInitCreateRequestDto();
        dto.setAdminEmail("admin@qwerty.io");
        dto.setAdminNm("쿼티초기사용자");
        dto.setAdminRole(AdminRole.SUPER);
        dto.setOrgCd("123-45-67890");
        dto.setOrgNm("쿼티시스템");
        dto.setLoginId("admin");
        dto.setLoginPw("admin");

        // when (call api)
        mockMvc.perform(post(ApiConstants.Path.ADMIN_PATH + "/initialize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").doesNotExist()) // null check
                .andDo(print());

        // then (the end)
    }

    @Test
    @DisplayName("관리자 생성 테스트")
    public void registerAdmin(){
        IntStream.range(0, 10).forEach(i -> {
            // given
            AdminCreateRequestDto dto = new AdminCreateRequestDto();
            dto.setLoginId(String.format("admin%s", i));
            dto.setLoginPw("admin");
            dto.setAdminRole(AdminRole.ADMIN);
            dto.setAdminEmail(String.format("admin%s@admin.com", i));
            dto.setAdminNm(String.format("adminName%s", i + 1));
            dto.setOrgId(1);

            // when
            try {
                mockMvc.perform(post(ApiConstants.Path.ADMIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.success").value(true))
                        .andExpect(jsonPath("$.data").doesNotExist()) // null check
                        .andDo(print());
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                // then
                System.out.println("TEST COMPLETE");
            }
        });
    }

    @Test
    @DisplayName("관리자 수정 테스트")
    public void updateAdmin() throws Exception {
        // given
        AdminUpdateRequestDto dto = new AdminUpdateRequestDto();
        dto.setAdminEmail("update@qwerty.io");
        dto.setAdminNm("수정된이름");

        // when
        mockMvc.perform(patch(ApiConstants.Path.ADMIN_PATH + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").doesNotExist()) // null check
                .andDo(print());

        // then
    }

    @Test
    @DisplayName("한건의 관리자 조회 테스트")
    public void selectOneAdmin() throws Exception{

    }

    @Test
    @DisplayName("관리자 목록 조회 테스트")
    public void selectAllAdmin() throws Exception{

    }
}
