package com.qwerty.nexus.domain.management.organization.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qwerty.nexus.domain.management.organization.entity.OrganizationEntity;
import com.qwerty.nexus.domain.management.organization.repository.OrganizationRepository;
import com.qwerty.nexus.domain.management.organization.service.OrganizationService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.filter.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrganizationController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(OrganizationService.class)
class OrganizationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrganizationRepository organizationRepository;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void updateOrganization_success() throws Exception {
        given(organizationRepository.updateOrganization(any())).willReturn(1);

        String body = objectMapper.writeValueAsString(Map.of(
                "orgNm", "변경된단체명",
                "orgCd", "111-22-33333",
                "updatedBy", "superadmin"
        ));

        mockMvc.perform(patch(ApiConstants.Path.ORG_PATH + "/{orgId}", 10)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("단체 정보 수정 성공."));
    }

    @Test
    void getOrganization_success() throws Exception {
        given(organizationRepository.findByOrgId(10)).willReturn(OrganizationEntity.builder()
                .orgId(10)
                .orgNm("넥서스")
                .orgCd("123-45-67890")
                .isDel("N")
                .build());

        mockMvc.perform(get(ApiConstants.Path.ORG_PATH + "/{orgId}", 10))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.orgId").value(10))
                .andExpect(jsonPath("$.data.orgNm").value("넥서스"));
    }

    @Test
    void getOrganization_notFound() throws Exception {
        given(organizationRepository.findByOrgId(99)).willReturn(null);

        mockMvc.perform(get(ApiConstants.Path.ORG_PATH + "/{orgId}", 99))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("단체 정보 존재하지 않음."));
    }
}
