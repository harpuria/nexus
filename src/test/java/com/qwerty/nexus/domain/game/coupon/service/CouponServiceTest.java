package com.qwerty.nexus.domain.game.coupon.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qwerty.nexus.domain.game.coupon.command.CouponCreateCommand;
import com.qwerty.nexus.domain.game.coupon.command.CouponGrantCommand;
import com.qwerty.nexus.domain.game.coupon.command.CouponUpdateCommand;
import com.qwerty.nexus.domain.game.coupon.entity.CouponEntity;
import com.qwerty.nexus.domain.game.coupon.entity.CouponUseLogEntity;
import com.qwerty.nexus.domain.game.coupon.repository.CouponRepository;
import com.qwerty.nexus.domain.game.coupon.repository.CouponUseLogRepository;
import com.qwerty.nexus.domain.game.data.currency.entity.CurrencyEntity;
import com.qwerty.nexus.domain.game.data.currency.entity.UserCurrencyEntity;
import com.qwerty.nexus.domain.game.data.currency.repository.CurrencyRepository;
import com.qwerty.nexus.domain.game.data.currency.repository.UserCurrencyRepository;
import com.qwerty.nexus.global.response.Result;
import org.jooq.JSONB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private CouponUseLogRepository couponUseLogRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private UserCurrencyRepository userCurrencyRepository;

    private ObjectMapper objectMapper;

    private CouponService couponService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        couponService = new CouponService(couponRepository, couponUseLogRepository, currencyRepository, userCurrencyRepository, objectMapper);
    }

    @Test
    @DisplayName("쿠폰 생성 성공")
    void createCoupon_success() {
        CouponCreateCommand command = CouponCreateCommand.builder()
                .gameId(1)
                .name("쿠폰")
                .code("CODE")
                .build();

        doReturn(CouponEntity.builder().couponId(1).build()).when(couponRepository).create(any());

        Result<Void> result = couponService.create(command);

        assertInstanceOf(Result.Success.class, result);
        assertThat(((Result.Success<Void>) result).message()).isEqualTo("쿠폰 생성 완료");
        verify(couponRepository).create(any(CouponEntity.class));
    }

    @Test
    @DisplayName("쿠폰 생성 실패")
    void createCoupon_failure() {
        CouponCreateCommand command = CouponCreateCommand.builder()
                .gameId(1)
                .name("쿠폰")
                .code("CODE")
                .build();

        doReturn(CouponEntity.builder().build()).when(couponRepository).create(any());

        Result<Void> result = couponService.create(command);

        assertInstanceOf(Result.Failure.class, result);
    }

    @Test
    @DisplayName("쿠폰 수정 성공")
    void updateCoupon_success() {
        CouponUpdateCommand command = CouponUpdateCommand.builder()
                .couponId(1)
                .name("수정")
                .build();

        doReturn(Optional.of(CouponEntity.builder().couponId(1).build())).when(couponRepository).update(any());

        Result<Void> result = couponService.update(command);

        assertInstanceOf(Result.Success.class, result);
        assertThat(((Result.Success<Void>) result).message()).contains("성공");
    }

    @Test
    @DisplayName("쿠폰 수정 실패")
    void updateCoupon_failure() {
        CouponUpdateCommand command = CouponUpdateCommand.builder()
                .couponId(1)
                .name("수정")
                .build();

        doReturn(Optional.empty()).when(couponRepository).update(any());

        Result<Void> result = couponService.update(command);

        assertInstanceOf(Result.Failure.class, result);
    }

    @Test
    @DisplayName("쿠폰 지급 성공")
    void grantCoupon_success() throws Exception {
        OffsetDateTime now = OffsetDateTime.now();
        CouponEntity coupon = CouponEntity.builder()
                .couponId(1)
                .code("CODE")
                .isDel("N")
                .startDate(now.minusDays(1))
                .endDate(now.plusDays(1))
                .maxIssueCount(10L)
                .useLimitPerUser(2)
                .rewards(JSONB.valueOf("[{\"currencyId\":1,\"amount\":100}]"))
                .build();

        doReturn(Optional.of(coupon)).when(couponRepository).findByCode("CODE");
        doReturn(1L).when(couponRepository).countTotalUses(1);
        doReturn(1L).when(couponRepository).countUserUses(1, 10);

        CurrencyEntity currency = CurrencyEntity.builder()
                .currencyId(1)
                .maxAmount(1000L)
                .build();
        doReturn(Optional.of(currency)).when(currencyRepository).selectOne(any());

        UserCurrencyEntity userCurrency = UserCurrencyEntity.builder()
                .userId(10)
                .currencyId(1)
                .amount(100L)
                .build();
        doReturn(Optional.of(userCurrency)).when(userCurrencyRepository).selectUserCurrency(any());

        doReturn(CouponUseLogEntity.builder().logId(1).build()).when(couponUseLogRepository).create(any());

        CouponGrantCommand command = CouponGrantCommand.builder()
                .code("CODE")
                .userId(10)
                .requestedBy("admin")
                .build();

        Result<Void> result = couponService.grant(command);

        assertInstanceOf(Result.Success.class, result);
        verify(userCurrencyRepository).addCurrency(any(), eq(100L), eq(1));
        verify(couponUseLogRepository).create(any());
    }

    @Test
    @DisplayName("쿠폰 만료 기간 실패")
    void grantCoupon_expired_failure() throws Exception {
        OffsetDateTime now = OffsetDateTime.now();
        CouponEntity coupon = CouponEntity.builder()
                .couponId(1)
                .code("CODE")
                .isDel("N")
                .startDate(now.minusDays(3))
                .endDate(now.minusDays(1))
                .rewards(JSONB.valueOf("[]"))
                .build();

        doReturn(Optional.of(coupon)).when(couponRepository).findByCode("CODE");

        CouponGrantCommand command = CouponGrantCommand.builder()
                .code("CODE")
                .userId(10)
                .requestedBy("admin")
                .build();

        Result<Void> result = couponService.grant(command);

        assertInstanceOf(Result.Failure.class, result);
    }

    @Test
    @DisplayName("유저 쿠폰 제한 실패")
    void grantCoupon_userLimit_failure() throws Exception {
        OffsetDateTime now = OffsetDateTime.now();
        CouponEntity coupon = CouponEntity.builder()
                .couponId(1)
                .code("CODE")
                .isDel("N")
                .startDate(now.minusDays(1))
                .endDate(now.plusDays(1))
                .useLimitPerUser(1)
                .rewards(JSONB.valueOf("[]"))
                .build();

        doReturn(Optional.of(coupon)).when(couponRepository).findByCode("CODE");
        doReturn(1L).when(couponRepository).countUserUses(1, 10);

        CouponGrantCommand command = CouponGrantCommand.builder()
                .code("CODE")
                .userId(10)
                .requestedBy("admin")
                .build();

        Result<Void> result = couponService.grant(command);

        assertInstanceOf(Result.Failure.class, result);
    }

    @Test
    @DisplayName("재화 정보 없음 실패")
    void grantCoupon_missingCurrency_failure() throws Exception {
        OffsetDateTime now = OffsetDateTime.now();
        CouponEntity coupon = CouponEntity.builder()
                .couponId(1)
                .code("CODE")
                .isDel("N")
                .startDate(now.minusDays(1))
                .endDate(now.plusDays(1))
                .rewards(JSONB.valueOf("[{\"currencyId\":1,\"amount\":100}]"))
                .build();

        doReturn(Optional.of(coupon)).when(couponRepository).findByCode("CODE");
        doReturn(0L).when(couponRepository).countTotalUses(1);
        doReturn(0L).when(couponRepository).countUserUses(1, 10);
        doReturn(Optional.empty()).when(currencyRepository).selectOne(any());

        CouponGrantCommand command = CouponGrantCommand.builder()
                .code("CODE")
                .userId(10)
                .requestedBy("admin")
                .build();

        Result<Void> result = couponService.grant(command);

        assertInstanceOf(Result.Failure.class, result);
    }
}
