package com.qwerty.nexus.domain.game.data.currency.service;

import com.qwerty.nexus.domain.game.data.currency.command.CurrencyCreateCommand;
import com.qwerty.nexus.domain.game.data.currency.command.CurrencyUpdateCommand;
import com.qwerty.nexus.domain.game.data.currency.dto.response.CurrencyResponseDto;
import com.qwerty.nexus.domain.game.data.currency.entity.CurrencyEntity;
import com.qwerty.nexus.domain.game.data.currency.repository.CurrencyRepository;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.response.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyRepository repository;

    /**
     * 재화 정보 생성
     * @param command
     * @return
     */
    public Result<CurrencyResponseDto> createCurrency(CurrencyCreateCommand command){
        CurrencyResponseDto rst = new CurrencyResponseDto();

        CurrencyEntity entity = CurrencyEntity.builder()
                .gameId(command.getGameId())
                .name(command.getName())
                .desc(command.getDesc())
                .createdBy(command.getCreatedBy())
                .updatedBy(command.getCreatedBy())
                .maxAmount(command.getMaxAmount())
                .build();

        Optional<CurrencyEntity> createRst = Optional.ofNullable(repository.createCurrency(entity));
        if(createRst.isPresent()){
            rst.convertEntityToDto(createRst.get());
        }
        else{
            return Result.Failure.of("재화 생성 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(rst, "재화 생성 완료.");
    }

    /**
     * 재화 정보 수정
     * @param command
     * @return
     */
    public Result<CurrencyResponseDto> updateCurrency(CurrencyUpdateCommand command){
        CurrencyResponseDto rst = new CurrencyResponseDto();

        CurrencyEntity entity = CurrencyEntity.builder()
                .currencyId(command.getCurrencyId())
                .name(command.getName())
                .desc(command.getDesc())
                .updatedBy(command.getUpdatedBy())
                .isDel(command.getIsDel())
                .maxAmount(command.getMaxAmount())
                .build();

        String type = "수정";
        if(command.getIsDel() != null && command.getIsDel().equalsIgnoreCase("Y"))
            type = "삭제";

        Optional<CurrencyEntity> updateRst = Optional.ofNullable(repository.updateCurrency(entity));
        if(updateRst.isEmpty()) {
            return Result.Failure.of(String.format("재화 %s 실패.", type), ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(rst, String.format("재화 %s 완료.", type));
    }

    /**
     * 한 건의 재화 정보 조회
     * @param currencyId
     * @return
     */
    public Result<CurrencyResponseDto> selectOneCurrency(int currencyId){
        CurrencyResponseDto rst = new CurrencyResponseDto();
        CurrencyEntity entity = CurrencyEntity.builder()
                .currencyId(currencyId)
                .build();

        Optional<CurrencyEntity> selectRst = Optional.ofNullable(repository.selectOneCurrency(entity));
        if(selectRst.isPresent()){
            rst.convertEntityToDto(selectRst.get());
        }else{
            return Result.Failure.of("재화 정보 조회 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(rst, "재화 조회 완료");
    }
}
