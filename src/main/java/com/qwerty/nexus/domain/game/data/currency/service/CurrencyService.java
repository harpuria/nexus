package com.qwerty.nexus.domain.game.data.currency.service;

import com.qwerty.nexus.domain.game.data.currency.dto.request.CurrencyCreateRequestDto;
import com.qwerty.nexus.domain.game.data.currency.dto.request.CurrencyUpdateRequestDto;
import com.qwerty.nexus.domain.game.data.currency.dto.response.CurrencyListResponseDto;
import com.qwerty.nexus.domain.game.data.currency.dto.response.CurrencyResponseDto;
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
import com.qwerty.nexus.global.util.PagingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyRepository repository;
    private final GameUserRepository gameUserRepository;
    private final UserCurrencyRepository userCurrencyRepository;

    /**
     * 재화 정보 생성
     * @param dto
     * @return
     */
    @Transactional
    public Result<Void> createCurrency(CurrencyCreateRequestDto dto){
        CurrencyEntity entity = CurrencyEntity.builder()
                .gameId(dto.getGameId())
                .name(dto.getName())
                .desc(dto.getDesc())
                .createdBy(dto.getCreatedBy())
                .updatedBy(dto.getCreatedBy())
                .defaultAmount(dto.getDefaultAmount())
                .maxAmount(dto.getMaxAmount())
                .build();

        Integer createdCurrencyId = repository.insertCurrency(entity);
        if (createdCurrencyId == null) {
            return Result.Failure.of("재화 생성 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        // 재화 생성 후, 게임에 유저가 있을경우 각 유저들에게 유저재화 데이터 추가
        List<Integer> userIdList = gameUserRepository.findAllUserIdsByGameId(
                GameUserEntity.builder().gameId(dto.getGameId()).build()
        );

        for (Integer userId : userIdList) {
            UserCurrencyEntity userCurrencyEntity = UserCurrencyEntity.builder()
                    .userId(userId)
                    .amount(dto.getDefaultAmount()) // 기본 재화 수량 설정
                    .currencyId(createdCurrencyId)
                    .createdBy(dto.getCreatedBy())
                    .updatedBy(dto.getCreatedBy())
                    .build();

            userCurrencyRepository.insertUserCurrency(userCurrencyEntity);
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.CREATED);
    }

    /**
     * 재화 정보 수정
     * @param dto
     * @return
     */
    @Transactional
    public Result<Void> updateCurrency(CurrencyUpdateRequestDto dto){
        CurrencyEntity entity = CurrencyEntity.builder()
                .currencyId(dto.getCurrencyId())
                .name(dto.getName())
                .desc(dto.getDesc())
                .updatedBy(dto.getUpdatedBy())
                .maxAmount(dto.getMaxAmount())
                .defaultAmount(dto.getDefaultAmount())
                .build();

        int updateRstCnt = repository.updateCurrency(entity);
        if(updateRstCnt > 0) {
            return Result.Success.of(null, "재화 수정 완료.");
        }

        return Result.Failure.of("재화 수정 실패.", ErrorCode.INTERNAL_ERROR.getCode());
    }

    /**
     * 재화 정보 삭제 (논리적 삭제)
     * @param dto
     * @return
     */
    @Transactional
    public Result<Void> deleteCurrency(CurrencyUpdateRequestDto dto){
        CurrencyEntity entity = CurrencyEntity.builder()
                .currencyId(dto.getCurrencyId())
                .updatedBy(dto.getUpdatedBy())
                .isDel(dto.getIsDel())
                .build();

        int updateRstCnt = repository.updateCurrency(entity);
        if(updateRstCnt > 0) {
            return Result.Success.of(null, "재화 삭제 완료.");
        }

        return Result.Failure.of("재화 삭제 실패.", ErrorCode.INTERNAL_ERROR.getCode());
    }

    /**
     * 한 건의 재화 정보 조회
     * @param currencyId
     * @return
     */
    public Result<CurrencyResponseDto> getCurrency(int currencyId){
        CurrencyEntity entity = CurrencyEntity.builder()
                .currencyId(currencyId)
                .build();

        Optional<CurrencyEntity> currency = repository.findByCurrencyId(entity);
        if(currency.isPresent()){
            return Result.Success.of(CurrencyResponseDto.from(currency.get()), ApiConstants.Messages.Success.RETRIEVED);
        }

        return Result.Failure.of("재화 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
    }

    /**
     * 재화 목록 가져오기
     * @param pagingDto
     * @param gameId
     * @return
     */
    public Result<CurrencyListResponseDto> listCurrencies(PagingRequestDto pagingDto, Integer gameId) {
        PagingEntity pagingEntity = PagingUtil.getPagingEntity(pagingDto);
        if (pagingEntity == null) {
            return Result.Failure.of("페이징 정보가 올바르지 않습니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        int validatedSize = pagingEntity.getSize();
        int safePage = pagingEntity.getPage();

        List<CurrencyResponseDto> currencies = repository.findAllByGameId(pagingEntity, gameId).stream()
                .map(CurrencyResponseDto::from)
                .toList();

        long totalCount = repository.countByGameIdAndKeyword(pagingEntity, gameId);
        int totalPages = validatedSize == 0 ? 0 : (int) Math.ceil((double) totalCount / validatedSize);
        boolean hasNext = safePage + 1 < totalPages;
        boolean hasPrevious = safePage > 0 && totalPages > 0;

        CurrencyListResponseDto response = CurrencyListResponseDto.builder()
                .currencies(currencies)
                .page(safePage)
                .size(validatedSize)
                .totalCount(totalCount)
                .totalPages(totalPages)
                .hasNext(hasNext)
                .hasPrevious(hasPrevious)
                .build();

        return Result.Success.of(response, ApiConstants.Messages.Success.RETRIEVED);
    }
}
