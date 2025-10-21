package com.qwerty.nexus.domain.management.organization.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qwerty.nexus.domain.management.organization.dto.request.OrganizationUpdateRequestDto;
import com.qwerty.nexus.domain.management.organization.dto.response.OrganizationResponseDto;
import com.qwerty.nexus.domain.management.organization.service.OrganizationService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.response.Result;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrganizationController.class)
class OrganizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrganizationService organizationService;

    @Test
    @DisplayName("단체 수정 성공")
    void updateOrganization_success() throws Exception {
        OrganizationUpdateRequestDto request = new OrganizationUpdateRequestDto();
        request.setOrgNm("Org");

        when(organizationService.update(any())).thenReturn(Result.Success.of(null, "updated"));

        mockMvc.perform(patch(ApiConstants.Path.ORG_PATH + "/{orgId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("updated"))
                .andExpect(jsonPath("$.errorCode").doesNotExist());

        verify(organizationService).update(any());
    }

    @Test
    @DisplayName("단체 수정 실패")
    void updateOrganization_failure() throws Exception {
        OrganizationUpdateRequestDto request = new OrganizationUpdateRequestDto();
        request.setOrgNm("Org");

        when(organizationService.update(any())).thenReturn(Result.Failure.of("에러", "ERROR_CODE"));

        mockMvc.perform(patch(ApiConstants.Path.ORG_PATH + "/{orgId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }

    @Test
    @DisplayName("단체 조회 성공")
    void selectOneOrganization_success() throws Exception {
        OrganizationResponseDto response = OrganizationResponseDto.builder()
                .orgId(1)
                .orgNm("Org")
                .build();

        when(organizationService.selectOne(1)).thenReturn(Result.Success.of(response, "조회"));

        mockMvc.perform(get(ApiConstants.Path.ORG_PATH + "/{orgId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.orgNm").value("Org"));
    }

    @Test
    @DisplayName("단체 조회 실패")
    void selectOneOrganization_failure() throws Exception {
        when(organizationService.selectOne(1)).thenReturn(Result.Failure.of("에러", "ERROR_CODE"));

        mockMvc.perform(get(ApiConstants.Path.ORG_PATH + "/{orgId}", 1))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }
}
