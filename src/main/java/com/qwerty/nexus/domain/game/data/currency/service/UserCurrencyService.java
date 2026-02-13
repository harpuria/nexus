package com.qwerty.nexus.domain.game.data.currency.service;

import com.qwerty.nexus.domain.game.data.currency.dto.request.UserCurrencyCreateRequestDto;
import com.qwerty.nexus.domain.game.data.currency.dto.request.UserCurrencyOperateRequestDto;
import com.qwerty.nexus.domain.game.data.currency.dto.request.UserCurrencyUpdateRequestDto;
import com.qwerty.nexus.domain.game.data.currency.dto.response.UserCurrencyListResponseDto;
import com.qwerty.nexus.domain.game.data.currency.dto.response.UserCurrencyResponseDto;
import com.qwerty.nexus.domain.game.data.currency.entity.UserCurrencyEntity;
import com.qwerty.nexus.domain.game.data.currency.repository.UserCurrencyRepository;
import com.qwerty.nexus.domain.game.data.currency.result.UserCurrencyListResult;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.paging.dto.PagingRequestDto;
import com.qwerty.nexus.global.paging.entity.PagingEntity;
import com.qwerty.nexus.global.response.Result;
import com.qwerty.nexus.global.util.PagingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCurrencyService {
    private final UserCurrencyRepository repository;

    /**
     * 유저 재화 생성
     * @param dto
     * @return
     */
    @Transactional
    public Result<Void> createUserCurrency(UserCurrencyCreateRequestDto dto) {
        UserCurrencyEntity entity = UserCurrencyEntity.builder()
                .currencyId(dto.getCurrencyId())
                .userId(dto.getUserId())
                .createdBy(dto.getCreatedBy())
                .updatedBy(dto.getCreatedBy())
                .build();

        Integer createdUserCurrencyId = repository.insertUserCurrency(entity);
        if (createdUserCurrencyId == null) {
            return Result.Failure.of("유저 재화 생성 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.CREATED);
    }

    /**
     * 유저 재화 수정
     * @param dto
     * @return
     */
    @Transactional
    public Result<Void> updateUserCurrency(UserCurrencyUpdateRequestDto dto) {
        UserCurrencyEntity entity = UserCurrencyEntity.builder()
                .userCurrencyId(dto.getUserCurrencyId())
                .currencyId(dto.getCurrencyId())
                .userId(dto.getUserId())
                .amount(dto.getAmount())
                .updatedBy(dto.getUpdatedBy())
                .build();

        int updateRstCnt = repository.updateUserCurrency(entity);
        if(updateRstCnt > 0){
            return Result.Success.of(null, "유저 재화 수정 완료.");
        }

        return Result.Failure.of("유저 재화 수정 실패.", ErrorCode.INTERNAL_ERROR.getCode());
    }

    /**
     * 유저 재화 삭제 (논리적 삭제)
     * @param dto
     * @return
     */
    @Transactional
    public Result<Void> deleteUserCurrency(UserCurrencyUpdateRequestDto dto) {
        UserCurrencyEntity entity = UserCurrencyEntity.builder()
                .userCurrencyId(dto.getUserCurrencyId())
                .updatedBy(dto.getUpdatedBy())
                .isDel(dto.getIsDel())
                .build();

        int updateRstCnt = repository.updateUserCurrency(entity);
        if(updateRstCnt > 0){
            return Result.Success.of(null, "유저 재화 삭제 완료.");
        }

        return Result.Failure.of("유저 재화 삭제 실패.", ErrorCode.INTERNAL_ERROR.getCode());
    }

    /**
     * 유저 재화 연산
     * @param dto
     * @return
     */
    @Transactional
    public Result<UserCurrencyResponseDto> operateUserCurrency(UserCurrencyOperateRequestDto dto) {
        UserCurrencyEntity condition = UserCurrencyEntity.builder()
                .userId(dto.getUserId())
                .currencyId(dto.getCurrencyId())
                .build();

        Optional<UserCurrencyEntity> currentUserCurrency = repository.findByUserIdAndCurrencyId(condition);
        if (currentUserCurrency.isEmpty()) {
            return Result.Failure.of("유저 재화 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        UserCurrencyEntity current = currentUserCurrency.get();
        if (dto.getUserCurrencyId() != null && !dto.getUserCurrencyId().equals(current.getUserCurrencyId())) {
            return Result.Failure.of("요청한 유저 재화 정보가 일치하지 않습니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        long currentAmount = Optional.ofNullable(current.getAmount()).orElse(0L);
        long calculatedAmount;
        switch (dto.getOperation()) {
            case "+" -> calculatedAmount = currentAmount + dto.getOperateAmount();
            case "-" -> calculatedAmount = currentAmount - dto.getOperateAmount();
            case "*" -> calculatedAmount = currentAmount * dto.getOperateAmount();
            case "/" -> {
                if (dto.getOperateAmount() == 0) {
                    return Result.Failure.of("0으로 나눌 수 없습니다.", ErrorCode.INVALID_REQUEST.getCode());
                }
                calculatedAmount = currentAmount / dto.getOperateAmount();
            }
            default -> {
                return Result.Failure.of("지원하지 않는 연산자입니다.", ErrorCode.INVALID_REQUEST.getCode());
            }
        }

        if (calculatedAmount < 0) {
            return Result.Failure.of("재화 수량은 0 미만이 될 수 없습니다.", ErrorCode.CONFLICT.getCode());
        }

        UserCurrencyEntity updateEntity = UserCurrencyEntity.builder()
                .userCurrencyId(current.getUserCurrencyId())
                .amount(calculatedAmount)
                .updatedBy(dto.getUpdatedBy())
                .build();

        int updateRstCnt = repository.updateUserCurrency(updateEntity);
        if (updateRstCnt <= 0) {
            return Result.Failure.of("유저 재화 연산 반영에 실패했습니다.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        UserCurrencyResponseDto responseDto = UserCurrencyResponseDto.from(
                UserCurrencyEntity.builder()
                        .userCurrencyId(current.getUserCurrencyId())
                        .currencyId(current.getCurrencyId())
                        .userId(current.getUserId())
                        .amount(calculatedAmount)
                        .build()
        );

        return Result.Success.of(responseDto, ApiConstants.Messages.Success.PROCESSED);
    }

    public Result<UserCurrencyListResponseDto> listUserCurrencies(
            PagingRequestDto dto,
            Integer userId,
            Integer gameId,
            Integer currencyId
    ) {
        PagingEntity pagingEntity = PagingUtil.getPagingEntity(dto);
        if (pagingEntity == null) {
            return Result.Failure.of("페이징 정보가 올바르지 않습니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        int validatedSize = pagingEntity.getSize();
        int safePage = pagingEntity.getPage();

        List<UserCurrencyListResult> userCurrencies = repository.findAllByUserIdAndGameIdAndCurrencyId(
                pagingEntity,
                userId,
                gameId,
                currencyId
        );

        List<UserCurrencyResponseDto> currencyResponses = userCurrencies.stream()
                .map(UserCurrencyResponseDto::from)
                .toList();

        long totalCount = repository.countByUserIdAndGameIdAndCurrencyId(userId, gameId, currencyId);
        int totalPages = validatedSize == 0 ? 0 : (int) Math.ceil((double) totalCount / validatedSize);
        boolean hasNext = safePage + 1 < totalPages;
        boolean hasPrevious = safePage > 0 && totalPages > 0;

        UserCurrencyListResponseDto responseDto = UserCurrencyListResponseDto.builder()
                .userCurrencies(currencyResponses)
                .page(safePage)
                .size(validatedSize)
                .totalCount(totalCount)
                .totalPages(totalPages)
                .hasNext(hasNext)
                .hasPrevious(hasPrevious)
                .build();

        return Result.Success.of(responseDto, ApiConstants.Messages.Success.RETRIEVED);
    }
}
