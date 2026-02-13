package com.qwerty.nexus.domain.game.data.coupon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qwerty.nexus.domain.game.data.coupon.entity.CouponEntity;
import com.qwerty.nexus.domain.game.data.coupon.repository.CouponRepository;
import com.qwerty.nexus.domain.game.data.coupon.service.CouponService;
import com.qwerty.nexus.domain.game.data.currency.entity.CurrencyEntity;
import com.qwerty.nexus.domain.game.data.currency.entity.UserCurrencyEntity;
import com.qwerty.nexus.domain.game.data.currency.repository.CurrencyRepository;
import com.qwerty.nexus.domain.game.data.currency.repository.UserCurrencyRepository;
import com.qwerty.nexus.domain.game.user.entity.GameUserEntity;
import com.qwerty.nexus.domain.game.user.repository.GameUserRepository;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.filter.JwtAuthenticationFilter;
import org.jooq.JSONB;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CouponController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(CouponService.class)
class CouponControllerIntegrationTest {

    private static final int GAME_ID = 1;
    private static final int USER_ID = 100;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CouponRepository couponRepository;

    @MockitoBean
    private GameUserRepository gameUserRepository;

    @MockitoBean
    private CurrencyRepository currencyRepository;

    @MockitoBean
    private UserCurrencyRepository userCurrencyRepository;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void useCoupon_success() throws Exception {
        mockActiveUser();

        given(couponRepository.findByGameIdAndCode(eq(GAME_ID), eq("WELCOME2026")))
                .willReturn(Optional.of(CouponEntity.builder()
                        .couponId(10)
                        .gameId(GAME_ID)
                        .code("WELCOME2026")
                        .maxIssueCount(100L)
                        .useLimitPerUser(2)
                        .rewards(JSONB.jsonb("[{\"currencyId\":1,\"amount\":100}]"))
                        .build()));
        given(couponRepository.countByCouponId(10)).willReturn(0L);
        given(couponRepository.countByCouponIdAndUserId(10, USER_ID)).willReturn(0L);

        mockCurrency(1000L, 500L);

        given(userCurrencyRepository.updateUserCurrencyAmountAddByUserIdAndCurrencyId(any(), eq(100L), eq(1)))
                .willReturn(1);
        given(couponRepository.insertCouponUseLog(any()))
                .willReturn(1);

        mockMvc.perform(post(ApiConstants.Path.COUPON_PATH + "/use")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildUseCouponBody("WELCOME2026")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("쿠폰 사용이 완료되었습니다."));
    }

    @Test
    void useCoupon_periodExpired() throws Exception {
        mockActiveUser();
        given(couponRepository.findByGameIdAndCode(eq(GAME_ID), eq("EXPIRED2026")))
                .willReturn(Optional.empty());

        mockMvc.perform(post(ApiConstants.Path.COUPON_PATH + "/use")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildUseCouponBody("EXPIRED2026")))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("사용 가능한 쿠폰을 찾을 수 없습니다."))
                .andExpect(jsonPath("$.errorCode").value("SYS005"));
    }

    @Test
    void useCoupon_userLimitExceeded() throws Exception {
        mockActiveUser();

        given(couponRepository.findByGameIdAndCode(eq(GAME_ID), eq("LIMIT2026")))
                .willReturn(Optional.of(CouponEntity.builder()
                        .couponId(11)
                        .gameId(GAME_ID)
                        .code("LIMIT2026")
                        .maxIssueCount(100L)
                        .useLimitPerUser(1)
                        .rewards(JSONB.jsonb("[{\"currencyId\":1,\"amount\":100}]"))
                        .build()));
        given(couponRepository.countByCouponId(11)).willReturn(10L);
        given(couponRepository.countByCouponIdAndUserId(11, USER_ID)).willReturn(1L);

        mockMvc.perform(post(ApiConstants.Path.COUPON_PATH + "/use")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildUseCouponBody("LIMIT2026")))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("쿠폰 사용 가능 횟수를 초과했습니다."))
                .andExpect(jsonPath("$.errorCode").value("SYS007"));
    }

    @Test
    void useCoupon_currencyLimitExceeded() throws Exception {
        mockActiveUser();

        given(couponRepository.findByGameIdAndCode(eq(GAME_ID), eq("MAX2026")))
                .willReturn(Optional.of(CouponEntity.builder()
                        .couponId(12)
                        .gameId(GAME_ID)
                        .code("MAX2026")
                        .maxIssueCount(100L)
                        .useLimitPerUser(3)
                        .rewards(JSONB.jsonb("[{\"currencyId\":1,\"amount\":600}]"))
                        .build()));
        given(couponRepository.countByCouponId(12)).willReturn(0L);
        given(couponRepository.countByCouponIdAndUserId(12, USER_ID)).willReturn(0L);

        mockCurrency(1000L, 500L);

        mockMvc.perform(post(ApiConstants.Path.COUPON_PATH + "/use")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildUseCouponBody("MAX2026")))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("보유 가능한 최대 재화를 초과했습니다."))
                .andExpect(jsonPath("$.errorCode").value("SYS002"));
    }

    private void mockActiveUser() {
        given(gameUserRepository.findByGameIdAndUserId(any()))
                .willReturn(Optional.of(GameUserEntity.builder()
                        .gameId(GAME_ID)
                        .userId(USER_ID)
                        .isDel("N")
                        .isWithdrawal("N")
                        .build()));
    }

    private void mockCurrency(long maxAmount, long userAmount) {
        given(currencyRepository.findByCurrencyId(any()))
                .willReturn(Optional.of(CurrencyEntity.builder()
                        .currencyId(1)
                        .maxAmount(maxAmount)
                        .isDel("N")
                        .build()));

        given(userCurrencyRepository.findByUserIdAndCurrencyId(any()))
                .willReturn(Optional.of(UserCurrencyEntity.builder()
                        .userId(USER_ID)
                        .currencyId(1)
                        .amount(userAmount)
                        .isDel("N")
                        .build()));
    }

    private String buildUseCouponBody(String couponCode) throws Exception {
        return objectMapper.writeValueAsString(Map.of(
                "gameId", GAME_ID,
                "userId", USER_ID,
                "couponCode", couponCode
        ));
    }
}
