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

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    void createOrganization_success() throws Exception {
        given(organizationRepository.insertOrganization(any())).willReturn(10);

        String body = objectMapper.writeValueAsString(Map.of(
                "orgNm", "Studio X",
                "orgCd", "111-22-33333",
                "createdBy", "superadmin"
        ));

        mockMvc.perform(post(ApiConstants.Path.ORG_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(ApiConstants.Messages.Success.CREATED));
    }

    @Test
    void updateOrganization_success() throws Exception {
        given(organizationRepository.updateOrganization(any())).willReturn(1);

        String body = objectMapper.writeValueAsString(Map.of(
                "orgNm", "Updated Studio",
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
    void deleteOrganization_success() throws Exception {
        given(organizationRepository.deleteOrganization(any())).willReturn(1);

        mockMvc.perform(delete(ApiConstants.Path.ORG_PATH + "/{orgId}", 10))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(ApiConstants.Messages.Success.DELETED));
    }

    @Test
    void getOrganization_success() throws Exception {
        given(organizationRepository.findByOrgId(10)).willReturn(OrganizationEntity.builder()
                .orgId(10)
                .orgNm("Nexus Org")
                .orgCd("123-45-67890")
                .isDel("N")
                .build());

        mockMvc.perform(get(ApiConstants.Path.ORG_PATH + "/{orgId}", 10))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.orgId").value(10))
                .andExpect(jsonPath("$.data.orgNm").value("Nexus Org"));
    }

    @Test
    void getOrganization_notFound() throws Exception {
        given(organizationRepository.findByOrgId(99)).willReturn(null);

        mockMvc.perform(get(ApiConstants.Path.ORG_PATH + "/{orgId}", 99))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("단체 정보 존재하지 않음."));
    }

    @Test
    void listOrganizations_success() throws Exception {
        given(organizationRepository.findAllByPaging(any())).willReturn(List.of(
                OrganizationEntity.builder()
                        .orgId(10)
                        .orgNm("Nexus Org")
                        .orgCd("123-45-67890")
                        .isDel("N")
                        .build()
        ));
        given(organizationRepository.countActiveOrganizations()).willReturn(1L);

        mockMvc.perform(get(ApiConstants.Path.ORG_PATH + "/list")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalCount").value(1))
                .andExpect(jsonPath("$.data.organizations[0].orgId").value(10));
    }
}


