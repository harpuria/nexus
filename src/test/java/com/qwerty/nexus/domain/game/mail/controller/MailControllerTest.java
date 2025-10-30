package com.qwerty.nexus.domain.game.mail.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qwerty.nexus.domain.game.mail.MailTargetType;
import com.qwerty.nexus.domain.game.mail.dto.request.MailSendRequestDto;
import com.qwerty.nexus.domain.game.mail.dto.request.MailTemplateCreateRequestDto;
import com.qwerty.nexus.domain.game.mail.dto.response.MailSendResponseDto;
import com.qwerty.nexus.domain.game.mail.dto.response.MailTemplateResponseDto;
import com.qwerty.nexus.domain.game.mail.service.MailService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.response.Result;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MailController.class)
class MailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MailService mailService;

    @Test
    @DisplayName("메일 템플릿 생성 성공")
    void createTemplate_success() throws Exception {
        MailTemplateCreateRequestDto request = new MailTemplateCreateRequestDto();
        request.setTitle("이벤트 안내");
        request.setContent("새로운 이벤트를 확인하세요.");
        request.setRewardItem("EVENT_REWARD");

        MailTemplateResponseDto response = MailTemplateResponseDto.builder()
                .templateId(1L)
                .title("이벤트 안내")
                .content("새로운 이벤트를 확인하세요.")
                .rewardItem("EVENT_REWARD")
                .createdAt(OffsetDateTime.now())
                .build();

        when(mailService.createTemplate(any())).thenReturn(Result.Success.of(response, "템플릿 생성 완료"));

        mockMvc.perform(post(ApiConstants.Path.MAIL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("템플릿 생성 완료"))
                .andExpect(jsonPath("$.data.templateId").value(1L));

        verify(mailService).createTemplate(any());
    }

    @Test
    @DisplayName("메일 템플릿 생성 실패")
    void createTemplate_failure() throws Exception {
        MailTemplateCreateRequestDto request = new MailTemplateCreateRequestDto();
        request.setTitle("이벤트 안내");

        when(mailService.createTemplate(any())).thenReturn(Result.Failure.of("실패", "MAIL_TEMPLATE_ERROR"));

        mockMvc.perform(post(ApiConstants.Path.MAIL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("MAIL_TEMPLATE_ERROR"));
    }

    @Test
    @DisplayName("메일 발송 성공")
    void sendMail_success() throws Exception {
        MailSendRequestDto request = new MailSendRequestDto();
        request.setTemplateId(2L);
        request.setTargetType(MailTargetType.INDIVIDUAL);
        request.setRecipientIds(List.of(1L, 2L));

        MailSendResponseDto response = MailSendResponseDto.builder()
                .templateId(2L)
                .dispatchedCount(2L)
                .recipientIds(Set.of(1L, 2L))
                .build();

        when(mailService.send(any())).thenReturn(Result.Success.of(response, "메일 발송 완료"));

        mockMvc.perform(post(ApiConstants.Path.MAIL_PATH + "/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("메일 발송 완료"))
                .andExpect(jsonPath("$.data.templateId").value(2L));

        verify(mailService).send(any());
    }

    @Test
    @DisplayName("메일 발송 실패")
    void sendMail_failure() throws Exception {
        MailSendRequestDto request = new MailSendRequestDto();
        request.setTemplateId(2L);

        when(mailService.send(any())).thenReturn(Result.Failure.of("실패", "MAIL_SEND_ERROR"));

        mockMvc.perform(post(ApiConstants.Path.MAIL_PATH + "/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("MAIL_SEND_ERROR"));
    }
}
