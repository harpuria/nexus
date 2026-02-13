package com.qwerty.nexus.domain.game.data.coupon.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qwerty.nexus.domain.game.data.coupon.TimeLimitType;
import com.qwerty.nexus.domain.game.data.coupon.dto.CouponRewardInfo;
import com.qwerty.nexus.domain.game.data.coupon.dto.request.CouponCreateRequestDto;
import com.qwerty.nexus.domain.game.data.coupon.dto.request.CouponUpdateRequestDto;
import com.qwerty.nexus.domain.game.data.coupon.dto.request.UseCouponRequestDto;
import com.qwerty.nexus.domain.game.data.coupon.dto.response.CouponListResponseDto;
import com.qwerty.nexus.domain.game.data.coupon.dto.response.CouponResponseDto;
import com.qwerty.nexus.domain.game.data.coupon.entity.CouponEntity;
import com.qwerty.nexus.domain.game.data.coupon.entity.CouponUseLogEntity;
import com.qwerty.nexus.domain.game.data.coupon.repository.CouponRepository;
import com.qwerty.nexus.domain.game.data.currency.entity.CurrencyEntity;
import com.qwerty.nexus.domain.game.data.currency.entity.UserCurrencyEntity;
import com.qwerty.nexus.domain.game.data.currency.repository.CurrencyRepository;
import com.qwerty.nexus.domain.game.data.currency.repository.UserCurrencyRepository;
import com.qwerty.nexus.domain.game.user.entity.GameUserEntity;
import com.qwerty.nexus.domain.game.user.repository.GameUserRepository;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.paging.dto.PagingRequestDto;
import com.qwerty.nexus.global.paging.entity.PagingEntity;
import com.qwerty.nexus.global.response.Result;
import com.qwerty.nexus.global.util.CommonUtil;
import com.qwerty.nexus.global.util.PagingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final GameUserRepository gameUserRepository;
    private final CurrencyRepository currencyRepository;
    private final UserCurrencyRepository userCurrencyRepository;
    private final ObjectMapper objectMapper;

    /**
     * 쿠폰 생성
     * @param dto
     * @return
     */
    @Transactional
    public Result<Void> createCoupon(CouponCreateRequestDto dto) {
        String normalizedCode = CommonUtil.normalizeText(dto.getCode());
        if (normalizedCode == null) {
            return Result.Failure.of("쿠폰 코드는 공백일 수 없습니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        Result<Void> scheduleValidationResult = validateCouponSchedule(dto.getTimeLimitType(), dto.getUseStartDate(), dto.getUseEndDate());
        if (scheduleValidationResult != null) {
            return scheduleValidationResult;
        }

        if (!isValidIssuePolicy(dto.getMaxIssueCount(), dto.getUseLimitPerUser(), 0L, 0)) {
            return Result.Failure.of("쿠폰 발행량/유저 사용량 정책이 올바르지 않습니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        if (couponRepository.existsByGameIdAndCode(dto.getGameId(), normalizedCode)) {
            return Result.Failure.of("이미 사용 중인 쿠폰 코드입니다.", ErrorCode.DUPLICATE_KEY_ERROR.getCode());
        }

        CouponEntity entity = CouponEntity.builder()
                .gameId(dto.getGameId())
                .name(dto.getName())
                .desc(dto.getDesc())
                .code(normalizedCode)
                .rewards(dto.getRewards())
                .useStartDate(dto.getUseStartDate())
                .useEndDate(dto.getUseEndDate())
                .maxIssueCount(dto.getMaxIssueCount())
                .useLimitPerUser(dto.getUseLimitPerUser())
                .createdBy(dto.getCreatedBy())
                .updatedBy(dto.getUpdatedBy())
                .build();

        Optional<CouponEntity> createRst = Optional.ofNullable(couponRepository.insertCoupon(entity));
        if (createRst.isPresent()) {
            return Result.Success.of(null, ApiConstants.Messages.Success.CREATED);
        }

        return Result.Failure.of("쿠폰 생성 실패", ErrorCode.INTERNAL_ERROR.getCode());
    }

    /**
     * 쿠폰 수정
     * @param dto
     * @return
     */
    @Transactional
    public Result<Void> updateCoupon(CouponUpdateRequestDto dto) {
        if (dto.getCouponId() == null || dto.getCouponId() <= 0) {
            return Result.Failure.of("쿠폰 ID가 올바르지 않습니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        Optional<CouponEntity> couponOptional = couponRepository.findByCouponId(dto.getCouponId());
        if (couponOptional.isEmpty()) {
            return Result.Failure.of("쿠폰 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }
        CouponEntity coupon = couponOptional.get();

        Integer mergedGameId = dto.getGameId() != null ? dto.getGameId() : coupon.getGameId();
        String mergedCode = dto.getCode() != null ? CommonUtil.normalizeText(dto.getCode()) : coupon.getCode();
        OffsetDateTime mergedStartDate = dto.getUseStartDate() != null ? dto.getUseStartDate() : coupon.getUseStartDate();
        OffsetDateTime mergedEndDate = dto.getUseEndDate() != null ? dto.getUseEndDate() : coupon.getUseEndDate();
        Long mergedMaxIssueCount = dto.getMaxIssueCount() != null ? dto.getMaxIssueCount() : coupon.getMaxIssueCount();
        Integer mergedUseLimitPerUser = dto.getUseLimitPerUser() != null ? dto.getUseLimitPerUser() : coupon.getUseLimitPerUser();

        if (mergedCode == null) {
            return Result.Failure.of("쿠폰 코드는 공백일 수 없습니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        Result<Void> scheduleValidationResult = validateCouponSchedule(dto.getTimeLimitType(), mergedStartDate, mergedEndDate);
        if (scheduleValidationResult != null) {
            return scheduleValidationResult;
        }

        if (couponRepository.existsByGameIdAndCodeAndCouponIdNot(mergedGameId, mergedCode, dto.getCouponId())) {
            return Result.Failure.of("이미 사용 중인 쿠폰 코드입니다.", ErrorCode.DUPLICATE_KEY_ERROR.getCode());
        }

        long usedCount = couponRepository.countByCouponId(dto.getCouponId());
        int maxUseCountPerUser = couponRepository.findMaxUseCountPerUserByCouponId(dto.getCouponId());

        if (!isValidIssuePolicy(mergedMaxIssueCount, mergedUseLimitPerUser, usedCount, maxUseCountPerUser)) {
            return Result.Failure.of("쿠폰 발행량/유저 사용량 정책이 현재 사용 이력과 충돌합니다.", ErrorCode.CONFLICT.getCode());
        }

        CouponEntity entity = CouponEntity.builder()
                .couponId(dto.getCouponId())
                .gameId(dto.getGameId())
                .name(dto.getName())
                .desc(dto.getDesc())
                .code(dto.getCode() != null ? mergedCode : null)
                .rewards(dto.getRewards())
                .useStartDate(dto.getUseStartDate())
                .useEndDate(dto.getUseEndDate())
                .maxIssueCount(dto.getMaxIssueCount())
                .useLimitPerUser(dto.getUseLimitPerUser())
                .updatedBy(dto.getUpdatedBy())
                .isDel(dto.getIsDel())
                .build();

        Optional<CouponEntity> updateRst = Optional.ofNullable(couponRepository.updateCoupon(entity));
        if (updateRst.isEmpty()) {
            return Result.Failure.of("쿠폰 수정 실패", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.UPDATED);
    }

    /**
     * 쿠폰 삭제 (논리)
     * @param couponId
     * @return
     */
    @Transactional
    public Result<Void> deleteCoupon(Integer couponId) {
        if (couponId == null || couponId <= 0) {
            return Result.Failure.of("쿠폰 ID가 올바르지 않습니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        Optional<CouponEntity> coupon = couponRepository.findByCouponId(couponId);
        if (coupon.isEmpty()) {
            return Result.Failure.of("쿠폰 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        Optional<CouponEntity> deleteRst = Optional.ofNullable(couponRepository.deleteCoupon(couponId));
        if (deleteRst.isEmpty()) {
            return Result.Failure.of("쿠폰 삭제 실패", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.DELETED);
    }

    /**
     * 쿠폰 사용
     * @param dto
     * @return
     */
    @Transactional
    public Result<Void> useCoupon(UseCouponRequestDto dto) {
        String normalizedCode = CommonUtil.normalizeText(dto.getCouponCode());
        if (normalizedCode == null) {
            return Result.Failure.of("쿠폰 코드는 공백일 수 없습니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        Optional<GameUserEntity> gameUser = gameUserRepository.findByGameIdAndUserId(
                GameUserEntity.builder()
                        .gameId(dto.getGameId())
                        .userId(dto.getUserId())
                        .build()
        );

        if (gameUser.isEmpty()) {
            return Result.Failure.of("게임 유저 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        if ("Y".equalsIgnoreCase(gameUser.get().getIsDel()) || "Y".equalsIgnoreCase(gameUser.get().getIsWithdrawal())) {
            return Result.Failure.of("비활성화된 유저는 쿠폰을 사용할 수 없습니다.", ErrorCode.ACCOUNT_DISABLED.getCode());
        }

        Optional<CouponEntity> couponOptional = couponRepository.findByGameIdAndCode(dto.getGameId(), normalizedCode);
        if (couponOptional.isEmpty()) {
            return Result.Failure.of("사용 가능한 쿠폰을 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        CouponEntity coupon = couponOptional.get();
        if (coupon.getMaxIssueCount() == null || coupon.getUseLimitPerUser() == null) {
            return Result.Failure.of("쿠폰 사용 정책 정보가 올바르지 않습니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        long totalUsedCount = couponRepository.countByCouponId(coupon.getCouponId());
        if (coupon.getMaxIssueCount() > 0 && totalUsedCount >= coupon.getMaxIssueCount()) {
            return Result.Failure.of("쿠폰 발행 수량이 모두 소진되었습니다.", ErrorCode.CONFLICT.getCode());
        }

        long userUsedCount = couponRepository.countByCouponIdAndUserId(coupon.getCouponId(), dto.getUserId());
        if (userUsedCount >= coupon.getUseLimitPerUser()) {
            return Result.Failure.of("쿠폰 사용 가능 횟수를 초과했습니다.", ErrorCode.CONFLICT.getCode());
        }

        List<CouponRewardInfo> rewardInfos = parseRewardList(coupon);
        if (rewardInfos == null || rewardInfos.isEmpty()) {
            return Result.Failure.of("쿠폰 보상 정보가 올바르지 않습니다.", ErrorCode.INVALID_FORMAT.getCode());
        }

        for (CouponRewardInfo rewardInfo : rewardInfos) {
            if (rewardInfo.getCurrencyId() <= 0 || rewardInfo.getAmount() == null || rewardInfo.getAmount() <= 0) {
                return Result.Failure.of("쿠폰 보상 정보가 올바르지 않습니다.", ErrorCode.INVALID_REQUEST.getCode());
            }

            CurrencyEntity currencyCondition = CurrencyEntity.builder()
                    .currencyId(rewardInfo.getCurrencyId())
                    .build();
            Optional<CurrencyEntity> currency = currencyRepository.findByCurrencyId(currencyCondition);
            if (currency.isEmpty() || "Y".equalsIgnoreCase(currency.get().getIsDel())) {
                return Result.Failure.of("지급할 재화 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
            }

            UserCurrencyEntity userCurrencyCondition = UserCurrencyEntity.builder()
                    .userId(dto.getUserId())
                    .currencyId(rewardInfo.getCurrencyId())
                    .build();

            Optional<UserCurrencyEntity> userCurrency = userCurrencyRepository.findByUserIdAndCurrencyId(userCurrencyCondition);
            if (userCurrency.isEmpty()) {
                return Result.Failure.of("유저 재화 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
            }

            if ("Y".equalsIgnoreCase(userCurrency.get().getIsDel())) {
                return Result.Failure.of("비활성화된 유저 재화 정보입니다.", ErrorCode.CONFLICT.getCode());
            }

            long rewardAmount = rewardInfo.getAmount();
            long calculatedAmount = userCurrency.get().getAmount() + rewardAmount;
            Long maxAmount = currency.get().getMaxAmount();
            if (maxAmount != null && maxAmount < calculatedAmount) {
                return Result.Failure.of("보유 가능한 최대 재화를 초과했습니다.", ErrorCode.INVALID_REQUEST.getCode());
            }
        }

        for (CouponRewardInfo rewardInfo : rewardInfos) {
            UserCurrencyEntity updateCondition = UserCurrencyEntity.builder()
                    .userId(dto.getUserId())
                    .currencyId(rewardInfo.getCurrencyId())
                    .build();
            int updateCount = userCurrencyRepository.updateUserCurrencyAmountAddByUserIdAndCurrencyId(
                    updateCondition,
                    rewardInfo.getAmount(),
                    rewardInfo.getCurrencyId()
            );

            if (updateCount <= 0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return Result.Failure.of("쿠폰 보상 지급에 실패했습니다.", ErrorCode.CONFLICT.getCode());
            }
        }

        String actor = String.valueOf(dto.getUserId());
        CouponUseLogEntity couponUseLogEntity = CouponUseLogEntity.builder()
                .couponId(coupon.getCouponId())
                .userId(dto.getUserId())
                .usedAt(OffsetDateTime.now())
                .createdBy(actor)
                .updatedBy(actor)
                .build();

        Optional<CouponUseLogEntity> useLogInsertRst = Optional.ofNullable(couponRepository.insertCouponUseLog(couponUseLogEntity));
        if (useLogInsertRst.isEmpty()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.Failure.of("쿠폰 사용 로그 저장에 실패했습니다.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(null, "쿠폰 사용이 완료되었습니다.");
    }

    /**
     * 쿠폰 한건 가져오기
     * @param couponId
     * @return
     */
    @Transactional(readOnly = true)
    public Result<CouponResponseDto> getCoupon(Integer couponId) {
        if (couponId == null || couponId <= 0) {
            return Result.Failure.of("쿠폰 ID가 올바르지 않습니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        Optional<CouponEntity> coupon = couponRepository.findByCouponId(couponId);
        if (coupon.isEmpty()) {
            return Result.Failure.of("쿠폰 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        return Result.Success.of(CouponResponseDto.from(coupon.get()), ApiConstants.Messages.Success.RETRIEVED);
    }

    /**
     * 쿠폰 목록 가져오기
     * @param dto
     * @param gameId
     * @return
     */
    @Transactional(readOnly = true)
    public Result<CouponListResponseDto> listCoupons(PagingRequestDto dto, Integer gameId) {
        if (gameId == null || gameId <= 0) {
            return Result.Failure.of("gameId가 올바르지 않습니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        PagingEntity pagingEntity = PagingUtil.getPagingEntity(dto);
        if (pagingEntity == null) {
            return Result.Failure.of("페이징 정보가 올바르지 않습니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        int validatedSize = pagingEntity.getSize();
        int safePage = pagingEntity.getPage();

        List<CouponResponseDto> coupons = couponRepository.findAllByGameIdAndKeyword(pagingEntity, gameId).stream()
                .map(CouponResponseDto::from)
                .toList();

        long totalCount = couponRepository.countByGameIdAndKeyword(pagingEntity, gameId);
        int totalPages = validatedSize == 0 ? 0 : (int) Math.ceil((double) totalCount / validatedSize);
        boolean hasNext = safePage + 1 < totalPages;
        boolean hasPrevious = safePage > 0 && totalPages > 0;

        CouponListResponseDto response = CouponListResponseDto.builder()
                .coupons(coupons)
                .page(safePage)
                .size(validatedSize)
                .totalCount(totalCount)
                .totalPages(totalPages)
                .hasNext(hasNext)
                .hasPrevious(hasPrevious)
                .build();

        return Result.Success.of(response, ApiConstants.Messages.Success.RETRIEVED);
    }

    private boolean isValidDateRange(OffsetDateTime startDate, OffsetDateTime endDate) {
        return startDate != null && endDate != null && !endDate.isBefore(startDate);
    }

    private Result<Void> validateCouponSchedule(TimeLimitType timeLimitType, OffsetDateTime startDate, OffsetDateTime endDate) {
        if (!TimeLimitType.LIMITED.equals(timeLimitType)) {
            return null;
        }

        if (!isValidDateRange(startDate, endDate)) {
            return Result.Failure.of("쿠폰 기간이 올바르지 않습니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        return null;
    }

    private boolean isValidIssuePolicy(Long maxIssueCount, Integer useLimitPerUser, long usedCount, int maxUseCountPerUser) {
        if (maxIssueCount == null || useLimitPerUser == null) {
            return false;
        }

        if (maxIssueCount < 0 || useLimitPerUser <= 0) {
            return false;
        }

        if (maxIssueCount > 0 && useLimitPerUser > maxIssueCount) {
            return false;
        }

        if (maxIssueCount > 0 && usedCount > maxIssueCount) {
            return false;
        }

        return maxUseCountPerUser <= useLimitPerUser;
    }

    private List<CouponRewardInfo> parseRewardList(CouponEntity couponEntity) {
        if (couponEntity.getRewards() == null || couponEntity.getRewards().data() == null) {
            return null;
        }

        try {
            return objectMapper.readValue(couponEntity.getRewards().data(), new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            log.error("쿠폰 보상 정보 파싱 실패. couponId={}", couponEntity.getCouponId(), e);
            return null;
        }
    }
}
