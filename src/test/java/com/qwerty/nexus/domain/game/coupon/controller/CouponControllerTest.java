package com.qwerty.nexus.domain.game.coupon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qwerty.nexus.domain.game.coupon.dto.request.CouponCreateRequestDto;
import com.qwerty.nexus.domain.game.coupon.dto.request.CouponGrantRequestDto;
import com.qwerty.nexus.domain.game.coupon.dto.request.CouponUpdateRequestDto;
import com.qwerty.nexus.domain.game.coupon.service.CouponService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.response.Result;
import org.jooq.JSONB;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CouponController.class)
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CouponService couponService;

    @Test
    @DisplayName("쿠폰 생성 성공")
    void createCoupon_success() throws Exception {
        CouponCreateRequestDto request = new CouponCreateRequestDto();
        request.setGameId(1);
        request.setName("신규 유저 웰컴 쿠폰");
        request.setCode("WELCOME100");
        request.setRewards(JSONB.valueOf("[{\\"currencyId\\":1,\\"amount\\":1000}]"));
        request.setCreatedBy("tester");

        when(couponService.create(any())).thenReturn(Result.Success.of(null, "쿠폰 생성 성공"));

        mockMvc.perform(post(ApiConstants.Path.COUPON_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("쿠폰 생성 성공"));
    }

    @Test
    @DisplayName("쿠폰 생성 실패")
    void createCoupon_failure() throws Exception {
        CouponCreateRequestDto request = new CouponCreateRequestDto();
        request.setGameId(1);
        request.setName("신규 유저 웰컴 쿠폰");

        when(couponService.create(any())).thenReturn(Result.Failure.of("생성 실패", "ERROR_CODE"));

        mockMvc.perform(post(ApiConstants.Path.COUPON_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"))
                .andExpect(jsonPath("$.message").value("생성 실패"));
    }

    @Test
    @DisplayName("쿠폰 수정 성공")
    void updateCoupon_success() throws Exception {
        CouponUpdateRequestDto request = new CouponUpdateRequestDto();
        request.setGameId(1);
        request.setName("기존 유저 리워드 쿠폰");
        request.setRewards(JSONB.valueOf("[{\\"currencyId\\":2,\\"amount\\":500}]"));
        request.setUpdatedBy("tester");

        when(couponService.update(any())).thenReturn(Result.Success.of(null, "쿠폰 수정 성공"));

        mockMvc.perform(patch(ApiConstants.Path.COUPON_PATH + "/{couponId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("쿠폰 수정 성공"));
    }

    @Test
    @DisplayName("쿠폰 수정 실패")
    void updateCoupon_failure() throws Exception {
        CouponUpdateRequestDto request = new CouponUpdateRequestDto();
        request.setGameId(1);
        request.setName("기존 유저 리워드 쿠폰");

        when(couponService.update(any())).thenReturn(Result.Failure.of("수정 실패", "ERROR_CODE"));

        mockMvc.perform(patch(ApiConstants.Path.COUPON_PATH + "/{couponId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"))
                .andExpect(jsonPath("$.message").value("수정 실패"));
    }

    @Test
    @DisplayName("쿠폰 지급 성공")
    void grantCoupon_success() throws Exception {
        CouponGrantRequestDto request = new CouponGrantRequestDto();
        request.setUserId(1);
        request.setCode("WELCOME100");
        request.setRequestedBy("tester");

        when(couponService.grant(any())).thenReturn(Result.Success.of(null, "쿠폰 지급 성공"));

        mockMvc.perform(post(ApiConstants.Path.COUPON_PATH + "/grant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("쿠폰 지급 성공"));
    }

    @Test
    @DisplayName("쿠폰 지급 실패")
    void grantCoupon_failure() throws Exception {
        CouponGrantRequestDto request = new CouponGrantRequestDto();
        request.setUserId(1);
        request.setCode("WELCOME100");

        when(couponService.grant(any())).thenReturn(Result.Failure.of("지급 실패", "ERROR_CODE"));

        mockMvc.perform(post(ApiConstants.Path.COUPON_PATH + "/grant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"))
                .andExpect(jsonPath("$.message").value("지급 실패"));
    }
}
