package com.qwerty.nexus.domain.game.data.currency.service;

import com.qwerty.nexus.domain.game.data.currency.dto.request.CurrencyCreateRequestDto;
import com.qwerty.nexus.domain.game.data.currency.dto.request.CurrencyUpdateRequestDto;
import com.qwerty.nexus.domain.game.data.currency.dto.response.CurrencyResponseDto;
import com.qwerty.nexus.domain.game.data.currency.entity.CurrencyEntity;
import com.qwerty.nexus.domain.game.data.currency.entity.CurrencyListResponseDto;
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
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
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
    public Result<Void> createCurrency(CurrencyCreateRequestDto dto){
        CurrencyEntity entity = CurrencyEntity.builder()
                .gameId(dto.getGameId())
                .name(dto.getName())
                .desc(dto.getDesc())
                .createdBy(dto.getCreatedBy())
                .updatedBy(dto.getCreatedBy())
                .maxAmount(dto.getMaxAmount())
                .build();

        Optional<CurrencyEntity> createRst = Optional.ofNullable(repository.insertCurrency(entity));

        // 추가로 새로운 재화가 생성되었을 때 유저가 존재하는 경우, 기본 유저재화 정보는 생성해줘야 할듯.
        // 1) 다이아 라는 재화를 생성
        // 2) 유저가 10명이 이미 있다고 가정
        // 3) 해당 유저에게 다이아 재화 정보를 생성해준다

        // 역으로 새로운 유저가 생성되는 경우
        // 1) 유저 1이 생성
        // 2) 현재 존재하는 재화들 (삭제상태가 아닌 것)을 모두 해당 유저에게 생성하게 함

        if(createRst.isPresent()){
            List<Integer> userIdList = gameUserRepository.selectAllUserId(GameUserEntity.builder().gameId(dto.getGameId()).build());
            if(!userIdList.isEmpty()){
                userIdList.forEach(userId -> {
                    log.info("===========================");
                    log.info(userId);
                    log.info("===========================");

                    // 유저가 존재하면 모든 유저의 UserCurrency 에 신규 재화 데이터 넣어주기
                    // 이거 넣으려면 전체 유저의 id(pk)를 알아야하겠군
                    UserCurrencyEntity userCurrencyEntity = UserCurrencyEntity.builder()
                            .userId(userId)
                            .currencyId(createRst.get().getCurrencyId())
                            .createdBy(dto.getCreatedBy())
                            .updatedBy(dto.getCreatedBy())
                            .build();
                    userCurrencyRepository.insertUserCurrency(userCurrencyEntity);
                });
            }

            return Result.Success.of(null, "재화 생성 완료.");
        }
        else{
            return Result.Failure.of("재화 생성 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }
    }

    /**
     * 재화 정보 수정
     * @param dto
     * @return
     */
    public Result<Void> updateCurrency(CurrencyUpdateRequestDto dto){
        CurrencyEntity entity = CurrencyEntity.builder()
                .currencyId(dto.getCurrencyId())
                .name(dto.getName())
                .desc(dto.getDesc())
                .updatedBy(dto.getUpdatedBy())
                .isDel(dto.getIsDel())
                .maxAmount(dto.getMaxAmount())
                .build();

        String type = "수정";
        if(dto.getIsDel() != null && dto.getIsDel().equalsIgnoreCase("Y"))
            type = "삭제";

        Optional<CurrencyEntity> updateRst = Optional.ofNullable(repository.updateCurrency(entity));
        if(updateRst.isPresent()) {
            return Result.Success.of(null, String.format("재화 %s 완료.", type));
        }
        else{
            return Result.Failure.of(String.format("재화 %s 실패.", type), ErrorCode.INTERNAL_ERROR.getCode());
        }
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

        Optional<CurrencyEntity> selectRst = repository.findByCurrencyId(entity);
        if(selectRst.isPresent()){
            return Result.Success.of(CurrencyResponseDto.from(selectRst.get()), "재화 조회 완료");
        }else{
            return Result.Failure.of("재화 정보 조회 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }
    }

    /**
     * 재화 목록 가져오기
     * @param pagingDto
     * @param gameId
     * @return
     */
    public Result<CurrencyListResponseDto> listCurrencies(PagingRequestDto pagingDto, Integer gameId) {
        PagingEntity pagingEntity = PagingUtil.getPagingEntity(pagingDto);
        int validatedSize = pagingEntity.getSize();
        int safePage = pagingEntity.getPage();

        Optional<List<CurrencyEntity>> selectRst = Optional.ofNullable(repository.findAllByGameId(pagingEntity, gameId));
        if(selectRst.isEmpty()) {
            return Result.Failure.of("재화 목록이 존재하지 않음.",  ErrorCode.INTERNAL_ERROR.getCode());
        }

        List<CurrencyResponseDto> currencies = selectRst.get().stream().map(CurrencyResponseDto::from).toList();

        long totalCount = currencies.size();
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

        return Result.Success.of(response, "재화 목록 조회 완료.");
    }
}
