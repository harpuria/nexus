package com.qwerty.nexus.domain.game.mail.controller;

import com.qwerty.nexus.domain.game.mail.UserMailStatus;
import com.qwerty.nexus.domain.game.mail.dto.response.UserMailResponseDto;
import com.qwerty.nexus.domain.game.mail.service.UserMailService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.response.Result;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserMailController.class)
class UserMailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserMailService userMailService;

    @Test
    @DisplayName("유저 메일 목록 조회 성공")
    void getInbox_success() throws Exception {
        UserMailResponseDto response = UserMailResponseDto.builder()
                .userMailId(1L)
                .templateId(10L)
                .userId(100L)
                .title("Welcome")
                .content("Hello")
                .rewardItem("Item")
                .createdAt(OffsetDateTime.now())
                .status(UserMailStatus.UNREAD)
                .build();

        when(userMailService.getInbox(any())).thenReturn(Result.Success.of(List.of(response), "메일 조회 성공"));

        mockMvc.perform(get(ApiConstants.Path.USER_MAIL_PATH)
                        .param("userId", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("메일 조회 성공"))
                .andExpect(jsonPath("$.data[0].userMailId").value(1L))
                .andExpect(jsonPath("$.data[0].title").value("Welcome"));

        verify(userMailService).getInbox(any());
    }

    @Test
    @DisplayName("유저 메일 목록 조회 실패")
    void getInbox_failure() throws Exception {
        when(userMailService.getInbox(any())).thenReturn(Result.Failure.of("조회 실패", "ERROR_CODE"));

        mockMvc.perform(get(ApiConstants.Path.USER_MAIL_PATH)
                        .param("userId", "100"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("조회 실패"))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }

    @Test
    @DisplayName("유저 메일 읽음 처리 성공")
    void markAsRead_success() throws Exception {
        when(userMailService.markAsRead(any())).thenReturn(Result.Success.of(null, "읽음 처리 완료"));

        mockMvc.perform(patch(ApiConstants.Path.USER_MAIL_PATH + "/{userMailId}/read", 1L)
                        .param("userId", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("읽음 처리 완료"))
                .andExpect(jsonPath("$.data").doesNotExist());

        verify(userMailService).markAsRead(any());
    }

    @Test
    @DisplayName("유저 메일 읽음 처리 실패")
    void markAsRead_failure() throws Exception {
        when(userMailService.markAsRead(any())).thenReturn(Result.Failure.of("읽음 처리 실패", "ERROR_CODE"));

        mockMvc.perform(patch(ApiConstants.Path.USER_MAIL_PATH + "/{userMailId}/read", 1L)
                        .param("userId", "100"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("읽음 처리 실패"))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }

    @Test
    @DisplayName("유저 메일 수령 성공")
    void receive_success() throws Exception {
        UserMailResponseDto response = UserMailResponseDto.builder()
                .userMailId(1L)
                .templateId(10L)
                .userId(100L)
                .title("Reward")
                .content("Here is your reward")
                .rewardItem("Gold")
                .createdAt(OffsetDateTime.now())
                .receivedAt(OffsetDateTime.now())
                .status(UserMailStatus.RECEIVED)
                .build();

        when(userMailService.receive(any())).thenReturn(Result.Success.of(response, "수령 완료"));

        mockMvc.perform(patch(ApiConstants.Path.USER_MAIL_PATH + "/{userMailId}/receive", 1L)
                        .param("userId", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("수령 완료"))
                .andExpect(jsonPath("$.data.userMailId").value(1L))
                .andExpect(jsonPath("$.data.status").value(UserMailStatus.RECEIVED.name()));

        verify(userMailService).receive(any());
    }

    @Test
    @DisplayName("유저 메일 수령 실패")
    void receive_failure() throws Exception {
        when(userMailService.receive(any())).thenReturn(Result.Failure.of("수령 실패", "ERROR_CODE"));

        mockMvc.perform(patch(ApiConstants.Path.USER_MAIL_PATH + "/{userMailId}/receive", 1L)
                        .param("userId", "100"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("수령 실패"))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }

    @Test
    @DisplayName("유저 메일 삭제 성공")
    void delete_success() throws Exception {
        when(userMailService.delete(any())).thenReturn(Result.Success.of(null, "삭제 완료"));

        mockMvc.perform(delete(ApiConstants.Path.USER_MAIL_PATH + "/{userMailId}", 1L)
                        .param("userId", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("삭제 완료"))
                .andExpect(jsonPath("$.data").doesNotExist());

        verify(userMailService).delete(any());
    }

    @Test
    @DisplayName("유저 메일 삭제 실패")
    void delete_failure() throws Exception {
        when(userMailService.delete(any())).thenReturn(Result.Failure.of("삭제 실패", "ERROR_CODE"));

        mockMvc.perform(delete(ApiConstants.Path.USER_MAIL_PATH + "/{userMailId}", 1L)
                        .param("userId", "100"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("삭제 실패"))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }
}
