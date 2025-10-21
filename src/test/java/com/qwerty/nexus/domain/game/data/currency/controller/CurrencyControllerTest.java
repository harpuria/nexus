package com.qwerty.nexus.domain.game.data.currency.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qwerty.nexus.domain.game.data.currency.dto.request.CurrencyCreateRequestDto;
import com.qwerty.nexus.domain.game.data.currency.dto.request.CurrencyUpdateRequestDto;
import com.qwerty.nexus.domain.game.data.currency.dto.response.CurrencyResponseDto;
import com.qwerty.nexus.domain.game.data.currency.service.CurrencyService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CurrencyController.class)
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CurrencyService currencyService;

    @Test
    @DisplayName("재화 생성 성공")
    void createCurrency_success() throws Exception {
        CurrencyCreateRequestDto request = new CurrencyCreateRequestDto();
        request.setGameId(1);
        request.setName("골드");

        when(currencyService.create(any())).thenReturn(Result.Success.of(null, "created"));

        mockMvc.perform(post(ApiConstants.Path.CURRENCY_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("created"));

        verify(currencyService).create(any());
    }

    @Test
    @DisplayName("재화 생성 실패")
    void createCurrency_failure() throws Exception {
        CurrencyCreateRequestDto request = new CurrencyCreateRequestDto();

        when(currencyService.create(any())).thenReturn(Result.Failure.of("에러", "ERROR_CODE"));

        mockMvc.perform(post(ApiConstants.Path.CURRENCY_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }

    @Test
    @DisplayName("재화 수정 성공")
    void updateCurrency_success() throws Exception {
        CurrencyUpdateRequestDto request = new CurrencyUpdateRequestDto();
        request.setName("수정");

        when(currencyService.updateCurrency(any())).thenReturn(Result.Success.of(null, "updated"));

        mockMvc.perform(patch(ApiConstants.Path.CURRENCY_PATH + "/{currencyId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("updated"));
    }

    @Test
    @DisplayName("재화 수정 실패")
    void updateCurrency_failure() throws Exception {
        CurrencyUpdateRequestDto request = new CurrencyUpdateRequestDto();

        when(currencyService.updateCurrency(any())).thenReturn(Result.Failure.of("에러", "ERROR_CODE"));

        mockMvc.perform(patch(ApiConstants.Path.CURRENCY_PATH + "/{currencyId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }

    @Test
    @DisplayName("재화 삭제 성공")
    void deleteCurrency_success() throws Exception {
        when(currencyService.updateCurrency(any())).thenReturn(Result.Success.of(null, "deleted"));

        mockMvc.perform(delete(ApiConstants.Path.CURRENCY_PATH + "/{currencyId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("deleted"));
    }

    @Test
    @DisplayName("재화 삭제 실패")
    void deleteCurrency_failure() throws Exception {
        when(currencyService.updateCurrency(any())).thenReturn(Result.Failure.of("에러", "ERROR_CODE"));

        mockMvc.perform(delete(ApiConstants.Path.CURRENCY_PATH + "/{currencyId}", 1))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }

    @Test
    @DisplayName("재화 단건 조회 성공")
    void selectOneCurrency_success() throws Exception {
        CurrencyResponseDto response = CurrencyResponseDto.builder()
                .currencyId(1)
                .name("골드")
                .build();

        when(currencyService.selectOneCurrency(1)).thenReturn(Result.Success.of(response, "조회"));

        mockMvc.perform(get(ApiConstants.Path.CURRENCY_PATH + "/{currencyId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("골드"));
    }

    @Test
    @DisplayName("재화 단건 조회 실패")
    void selectOneCurrency_failure() throws Exception {
        when(currencyService.selectOneCurrency(1)).thenReturn(Result.Failure.of("에러", "ERROR_CODE"));

        mockMvc.perform(get(ApiConstants.Path.CURRENCY_PATH + "/{currencyId}", 1))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }
}
