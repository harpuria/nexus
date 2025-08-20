package com.qwerty.nexus.domain.gameData.currency.service;

import com.qwerty.nexus.domain.gameData.currency.command.CurrencyCreateCommand;
import com.qwerty.nexus.domain.gameData.currency.dto.response.CurrencyResponseDto;
import com.qwerty.nexus.domain.gameData.currency.entity.CurrencyEntity;
import com.qwerty.nexus.domain.gameData.currency.repository.CurrencyRepository;
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
            rst.convertEntityToDTO(createRst.get());
        }
        else{
            return Result.Failure.of("재화 생성에 실패하였습니다", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(rst, "재화 생성이 완료되었습니다.");
    }
}
