package com.qwerty.nexus.domain.game.data.coupon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qwerty.nexus.domain.game.data.coupon.entity.CouponEntity;
import com.qwerty.nexus.domain.game.data.coupon.repository.CouponRepository;
import com.qwerty.nexus.domain.game.data.coupon.service.CouponService;
import com.qwerty.nexus.domain.game.data.item.entity.ItemEntity;
import com.qwerty.nexus.domain.game.data.item.entity.UserItemStackEntity;
import com.qwerty.nexus.domain.game.data.item.repository.ItemRepository;
import com.qwerty.nexus.domain.game.data.item.repository.UserItemInstanceRepository;
import com.qwerty.nexus.domain.game.data.item.repository.UserItemStackRepository;
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
    private ItemRepository itemRepository;

    @MockitoBean
    private UserItemStackRepository userItemStackRepository;

    @MockitoBean
    private UserItemInstanceRepository userItemInstanceRepository;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void useCoupon_stackRewardSuccess() throws Exception {
        mockActiveUser();

        given(couponRepository.findByGameIdAndCode(eq(GAME_ID), eq("WELCOME2026")))
                .willReturn(Optional.of(CouponEntity.builder()
                        .couponId(10)
                        .gameId(GAME_ID)
                        .code("WELCOME2026")
                        .maxIssueCount(100L)
                        .useLimitPerUser(2)
                        .rewards(JSONB.jsonb("[{\"itemId\":1,\"amount\":100}]"))
                        .build()));
        given(couponRepository.countByCouponId(10)).willReturn(0L);
        given(couponRepository.countByCouponIdAndUserId(10, USER_ID)).willReturn(0L);

        mockStackItem(1000L, 500L);

        given(userItemStackRepository.updateUserItemAmountAddByUserIdAndItemId(any(), eq(100L)))
                .willReturn(1);
        given(couponRepository.insertCouponUseLog(any()))
                .willReturn(1);

        mockMvc.perform(post(ApiConstants.Path.COUPON_PATH + "/use")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildUseCouponBody("WELCOME2026")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void useCoupon_instanceRewardSuccess() throws Exception {
        mockActiveUser();

        given(couponRepository.findByGameIdAndCode(eq(GAME_ID), eq("INSTANCE2026")))
                .willReturn(Optional.of(CouponEntity.builder()
                        .couponId(13)
                        .gameId(GAME_ID)
                        .code("INSTANCE2026")
                        .maxIssueCount(100L)
                        .useLimitPerUser(2)
                        .rewards(JSONB.jsonb("[{\"itemId\":2,\"amount\":2}]"))
                        .build()));
        given(couponRepository.countByCouponId(13)).willReturn(0L);
        given(couponRepository.countByCouponIdAndUserId(13, USER_ID)).willReturn(0L);

        mockInstanceItem();

        given(userItemInstanceRepository.insertUserItemInstance(any()))
                .willReturn(1)
                .willReturn(2);
        given(couponRepository.insertCouponUseLog(any()))
                .willReturn(1);

        mockMvc.perform(post(ApiConstants.Path.COUPON_PATH + "/use")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildUseCouponBody("INSTANCE2026")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
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
                        .rewards(JSONB.jsonb("[{\"itemId\":1,\"amount\":100}]"))
                        .build()));
        given(couponRepository.countByCouponId(11)).willReturn(10L);
        given(couponRepository.countByCouponIdAndUserId(11, USER_ID)).willReturn(1L);

        mockMvc.perform(post(ApiConstants.Path.COUPON_PATH + "/use")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildUseCouponBody("LIMIT2026")))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("SYS007"));
    }

    @Test
    void useCoupon_stackLimitExceeded() throws Exception {
        mockActiveUser();

        given(couponRepository.findByGameIdAndCode(eq(GAME_ID), eq("MAX2026")))
                .willReturn(Optional.of(CouponEntity.builder()
                        .couponId(12)
                        .gameId(GAME_ID)
                        .code("MAX2026")
                        .maxIssueCount(100L)
                        .useLimitPerUser(3)
                        .rewards(JSONB.jsonb("[{\"itemId\":1,\"amount\":600}]"))
                        .build()));
        given(couponRepository.countByCouponId(12)).willReturn(0L);
        given(couponRepository.countByCouponIdAndUserId(12, USER_ID)).willReturn(0L);

        mockStackItem(1000L, 500L);

        mockMvc.perform(post(ApiConstants.Path.COUPON_PATH + "/use")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildUseCouponBody("MAX2026")))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
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

    private void mockStackItem(long maxStack, long userAmount) {
        given(itemRepository.findByItemId(any()))
                .willReturn(Optional.of(ItemEntity.builder()
                        .itemId(1)
                        .isStackable("Y")
                        .maxStack(maxStack)
                        .isDel("N")
                        .build()));

        given(userItemStackRepository.findByUserIdAndItemId(any()))
                .willReturn(Optional.of(UserItemStackEntity.builder()
                        .userId(USER_ID)
                        .itemId(1)
                        .amount(userAmount)
                        .isDel("N")
                        .build()));
    }

    private void mockInstanceItem() {
        given(itemRepository.findByItemId(any()))
                .willReturn(Optional.of(ItemEntity.builder()
                        .itemId(2)
                        .isStackable("N")
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
