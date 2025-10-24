package com.qwerty.nexus.domain.game.coupon.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qwerty.nexus.domain.game.coupon.command.CouponCreateCommand;
import com.qwerty.nexus.domain.game.coupon.command.CouponGrantCommand;
import com.qwerty.nexus.domain.game.coupon.command.CouponUpdateCommand;
import com.qwerty.nexus.domain.game.coupon.dto.CouponRewardInfo;
import com.qwerty.nexus.domain.game.coupon.entity.CouponEntity;
import com.qwerty.nexus.domain.game.coupon.entity.CouponUseLogEntity;
import com.qwerty.nexus.domain.game.coupon.repository.CouponRepository;
import com.qwerty.nexus.domain.game.coupon.repository.CouponUseLogRepository;
import com.qwerty.nexus.domain.game.data.currency.entity.CurrencyEntity;
import com.qwerty.nexus.domain.game.data.currency.entity.UserCurrencyEntity;
import com.qwerty.nexus.domain.game.data.currency.repository.CurrencyRepository;
import com.qwerty.nexus.domain.game.data.currency.repository.UserCurrencyRepository;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.response.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final CouponUseLogRepository couponUseLogRepository;
    private final CurrencyRepository currencyRepository;
    private final UserCurrencyRepository userCurrencyRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public Result<Void> create(CouponCreateCommand command) {
        CouponEntity entity = CouponEntity.builder()
                .gameId(command.getGameId())
                .name(command.getName())
                .desc(command.getDesc())
                .code(command.getCode())
                .rewards(command.getRewards())
                .startDate(command.getStartDate())
                .endDate(command.getEndDate())
                .maxIssueCount(command.getMaxIssueCount())
                .useLimitPerUser(command.getUseLimitPerUser())
                .createdBy(command.getCreatedBy())
                .updatedBy(command.getUpdatedBy())
                .isDel("N")
                .build();

        CouponEntity created = couponRepository.create(entity);
        if (created != null && created.getCouponId() != null) {
            return Result.Success.of(null, "쿠폰 생성 완료");
        }
        return Result.Failure.of("쿠폰 생성 실패", ErrorCode.INTERNAL_ERROR.getCode());
    }

    public Result<Void> update(CouponUpdateCommand command) {
        if (command.getCouponId() == null) {
            return Result.Failure.of("쿠폰 식별자 없음", ErrorCode.INVALID_REQUEST.getCode());
        }

        CouponEntity entity = CouponEntity.builder()
                .couponId(command.getCouponId())
                .gameId(command.getGameId())
                .name(command.getName())
                .desc(command.getDesc())
                .code(command.getCode())
                .rewards(command.getRewards())
                .startDate(command.getStartDate())
                .endDate(command.getEndDate())
                .maxIssueCount(command.getMaxIssueCount())
                .useLimitPerUser(command.getUseLimitPerUser())
                .updatedBy(command.getUpdatedBy())
                .isDel(command.getIsDel())
                .build();

        Optional<CouponEntity> result = couponRepository.update(entity);
        String action = (command.getIsDel() != null && command.getIsDel().equalsIgnoreCase("Y")) ? "삭제" : "수정";

        if (result.isPresent()) {
            return Result.Success.of(null, String.format("쿠폰 %s 성공", action));
        }
        return Result.Failure.of(String.format("쿠폰 %s 실패", action), ErrorCode.INTERNAL_ERROR.getCode());
    }

    @Transactional
    public Result<Void> grant(CouponGrantCommand command) throws JsonProcessingException {
        if (command.getUserId() == null) {
            return Result.Failure.of("유저 정보 없음", ErrorCode.INVALID_REQUEST.getCode());
        }

        Optional<CouponEntity> couponOptional = couponRepository.findByCode(command.getCode());
        if (couponOptional.isEmpty()) {
            return Result.Failure.of("쿠폰 정보 없음", ErrorCode.NOT_FOUND.getCode());
        }

        CouponEntity coupon = couponOptional.get();
        if ("Y".equalsIgnoreCase(coupon.getIsDel())) {
            return Result.Failure.of("삭제된 쿠폰", ErrorCode.INVALID_REQUEST.getCode());
        }

        OffsetDateTime now = OffsetDateTime.now();
        if (coupon.getStartDate() != null && now.isBefore(coupon.getStartDate())) {
            return Result.Failure.of("쿠폰 사용 가능 시간이 아님", ErrorCode.INVALID_REQUEST.getCode());
        }
        if (coupon.getEndDate() != null && now.isAfter(coupon.getEndDate())) {
            return Result.Failure.of("쿠폰 사용 기간 만료", ErrorCode.INVALID_REQUEST.getCode());
        }

        if (coupon.getMaxIssueCount() != null && coupon.getMaxIssueCount() > 0) {
            long totalUses = couponRepository.countTotalUses(coupon.getCouponId());
            if (totalUses >= coupon.getMaxIssueCount()) {
                return Result.Failure.of("쿠폰 발급 한도 초과", ErrorCode.INVALID_REQUEST.getCode());
            }
        }

        if (coupon.getUseLimitPerUser() != null && coupon.getUseLimitPerUser() > 0) {
            long userUses = couponRepository.countUserUses(coupon.getCouponId(), command.getUserId());
            if (userUses >= coupon.getUseLimitPerUser()) {
                return Result.Failure.of("유저 쿠폰 사용 한도 초과", ErrorCode.INVALID_REQUEST.getCode());
            }
        }

        if (coupon.getRewards() == null) {
            return Result.Failure.of("쿠폰 보상 정보 없음", ErrorCode.INTERNAL_ERROR.getCode());
        }

        List<CouponRewardInfo> rewards = objectMapper.readValue(coupon.getRewards().data(), new TypeReference<List<CouponRewardInfo>>() {});
        for (CouponRewardInfo reward : rewards) {
            CurrencyEntity currencyEntity = CurrencyEntity.builder()
                    .currencyId(reward.getCurrencyId())
                    .build();
            Optional<CurrencyEntity> currencyInfo = currencyRepository.selectOne(currencyEntity);
            if (currencyInfo.isEmpty()) {
                return Result.Failure.of("재화 정보 없음", ErrorCode.NOT_FOUND.getCode());
            }

            UserCurrencyEntity userCurrencyEntity = UserCurrencyEntity.builder()
                    .userId(command.getUserId())
                    .currencyId(reward.getCurrencyId())
                    .build();
            Optional<UserCurrencyEntity> userCurrencyInfo = userCurrencyRepository.selectUserCurrency(userCurrencyEntity);
            if (userCurrencyInfo.isEmpty()) {
                return Result.Failure.of("유저 재화 정보 없음", ErrorCode.NOT_FOUND.getCode());
            }

            long currentAmount = Optional.ofNullable(userCurrencyInfo.get().getAmount()).orElse(0L);
            long calculatedAmount = currentAmount + reward.getAmount();
            Long maxAmount = currencyInfo.get().getMaxAmount();
            if (maxAmount != null && maxAmount > 0 && calculatedAmount > maxAmount) {
                return Result.Failure.of("보유가능한 최대 재화 초과", ErrorCode.INVALID_REQUEST.getCode());
            }

            userCurrencyRepository.addCurrency(userCurrencyEntity, reward.getAmount(), reward.getCurrencyId());
        }

        couponUseLogRepository.create(CouponUseLogEntity.builder()
                .couponId(coupon.getCouponId())
                .userId(command.getUserId())
                .usedAt(now)
                .createdAt(now)
                .createdBy(command.getRequestedBy())
                .updatedAt(now)
                .updatedBy(command.getRequestedBy())
                .isDel("N")
                .build());

        return Result.Success.of(null, "쿠폰 지급 성공");
    }
}
