package com.qwerty.nexus.domain.game.data.currency.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qwerty.nexus.domain.game.data.currency.dto.request.UserCurrencyCreateRequestDto;
import com.qwerty.nexus.domain.game.data.currency.dto.request.UserCurrencyOperateRequestDto;
import com.qwerty.nexus.domain.game.data.currency.dto.request.UserCurrencyUpdateRequestDto;
import com.qwerty.nexus.domain.game.data.currency.dto.response.UserCurrencyResponseDto;
import com.qwerty.nexus.domain.game.data.currency.service.UserCurrencyService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.response.Result;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserCurrencyController.class)
class UserCurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserCurrencyService userCurrencyService;

    @Test
    @DisplayName("유저 재화 생성 성공")
    void createUserCurrency_success() throws Exception {
        UserCurrencyCreateRequestDto request = new UserCurrencyCreateRequestDto();
        request.setCurrencyId(1);
        request.setUserId(1);

        when(userCurrencyService.create(any())).thenReturn(Result.Success.of(null, "created"));

        mockMvc.perform(post(ApiConstants.Path.USER_CURRENCY_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("created"));

        verify(userCurrencyService).create(any());
    }

    @Test
    @DisplayName("유저 재화 생성 실패")
    void createUserCurrency_failure() throws Exception {
        UserCurrencyCreateRequestDto request = new UserCurrencyCreateRequestDto();

        when(userCurrencyService.create(any())).thenReturn(Result.Failure.of("에러", "ERROR_CODE"));

        mockMvc.perform(post(ApiConstants.Path.USER_CURRENCY_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }

    @Test
    @DisplayName("유저 재화 수정 성공")
    void updateUserCurrency_success() throws Exception {
        UserCurrencyUpdateRequestDto request = new UserCurrencyUpdateRequestDto();
        request.setUpdatedBy("tester");

        when(userCurrencyService.update(any())).thenReturn(Result.Success.of(null, "updated"));

        mockMvc.perform(patch(ApiConstants.Path.USER_CURRENCY_PATH + "/{userCurrencyId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("updated"));
    }

    @Test
    @DisplayName("유저 재화 수정 실패")
    void updateUserCurrency_failure() throws Exception {
        UserCurrencyUpdateRequestDto request = new UserCurrencyUpdateRequestDto();

        when(userCurrencyService.update(any())).thenReturn(Result.Failure.of("에러", "ERROR_CODE"));

        mockMvc.perform(patch(ApiConstants.Path.USER_CURRENCY_PATH + "/{userCurrencyId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }

    @Test
    @DisplayName("유저 재화 삭제 성공")
    void deleteUserCurrency_success() throws Exception {
        when(userCurrencyService.update(any())).thenReturn(Result.Success.of(null, "deleted"));

        mockMvc.perform(delete(ApiConstants.Path.USER_CURRENCY_PATH + "/{userCurrencyId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("deleted"));
    }

    @Test
    @DisplayName("유저 재화 삭제 실패")
    void deleteUserCurrency_failure() throws Exception {
        when(userCurrencyService.update(any())).thenReturn(Result.Failure.of("에러", "ERROR_CODE"));

        mockMvc.perform(delete(ApiConstants.Path.USER_CURRENCY_PATH + "/{userCurrencyId}", 1))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }

    @Test
    @DisplayName("유저 재화 연산 성공")
    void operationUserCurrency_success() throws Exception {
        UserCurrencyResponseDto response = UserCurrencyResponseDto.builder()
                .userCurrencyId(1)
                .amount(100L)
                .build();

        when(userCurrencyService.operate(any())).thenReturn(Result.Success.of(response, "operated"));

        mockMvc.perform(patch(ApiConstants.Path.USER_CURRENCY_PATH + "/operation/{userCurrencyId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserCurrencyOperateRequestDto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.amount").value(100));
    }

    @Test
    @DisplayName("유저 재화 연산 실패")
    void operationUserCurrency_failure() throws Exception {
        when(userCurrencyService.operate(any())).thenReturn(Result.Failure.of("에러", "ERROR_CODE"));

        mockMvc.perform(patch(ApiConstants.Path.USER_CURRENCY_PATH + "/operation/{userCurrencyId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserCurrencyOperateRequestDto())))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }
}
