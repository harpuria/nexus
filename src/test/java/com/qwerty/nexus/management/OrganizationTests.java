package com.qwerty.nexus.management;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qwerty.nexus.domain.management.game.dto.request.GameUpdateRequestDto;
import com.qwerty.nexus.domain.management.organization.dto.request.OrganizationUpdateRequestDto;
import com.qwerty.nexus.global.constant.ApiConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrganizationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("단체 정보 수정 테스트")
    public void updateOrg() throws Exception{
        // given
        OrganizationUpdateRequestDto dto = new OrganizationUpdateRequestDto();
        dto.setOrgNm("그리즐리소프트");
        dto.setUpdateBy("admin");

        // when
        mockMvc.perform(patch(ApiConstants.Path.ORG_PATH + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").doesNotExist()) // null check
                .andDo(print());

        // then
    }
}
