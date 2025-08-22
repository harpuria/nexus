package com.qwerty.nexus.domain.game.data.currency.controller;

import com.qwerty.nexus.domain.game.data.currency.command.CurrencyUpdateCommand;
import com.qwerty.nexus.domain.game.data.currency.dto.request.CurrencyCreateRequestDto;
import com.qwerty.nexus.domain.game.data.currency.dto.request.CurrencyUpdateRequestDto;
import com.qwerty.nexus.domain.game.data.currency.dto.response.CurrencyResponseDto;
import com.qwerty.nexus.domain.game.data.currency.service.CurrencyService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.response.ApiResponse;
import com.qwerty.nexus.global.response.ResponseEntityUtils;
import com.qwerty.nexus.global.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RequestMapping(ApiConstants.Path.CURRENCY_PATH)
@RestController
@RequiredArgsConstructor
@Tag(name = "재화", description = "재화 정보 관련 API")
public class CurrencyController {
    private final CurrencyService currencyService;

    /***
     * 재화 정보 생성
     * @param dto
     * @return
     */
    @PostMapping
    @Operation(summary = "재화 정보 생성")
    public ResponseEntity<ApiResponse<Void>> createCurrency(@RequestBody CurrencyCreateRequestDto dto){
        Result<CurrencyResponseDto> result = currencyService.createCurrency(dto.toCommand());

        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.CREATED);
    }

    /**
     * 재화 정보 수정
     * @param dto
     * @return
     */
    @PatchMapping("/{currencyId}")
    @Operation(summary = "재화 정보 수정")
    public ResponseEntity<ApiResponse<Void>> updateCurrency(@PathVariable("currencyId") int currencyId, @RequestBody CurrencyUpdateRequestDto dto){
        dto.setCurrencyId(currencyId);

        Result<CurrencyResponseDto> result = currencyService.updateCurrency(dto.toCommand());

        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 재화 정보 삭제
     * @param currencyId
     * @return
     */
    @DeleteMapping("/{currencyId}")
    @Operation(summary = "재화 정보 삭제")
    public ResponseEntity<ApiResponse<Void>> deleteCurrency(@PathVariable("currencyId") int currencyId){
        CurrencyUpdateRequestDto dto = new CurrencyUpdateRequestDto();
        dto.setCurrencyId(currencyId);
        dto.setIsDel("Y");

        Result<CurrencyResponseDto> result = currencyService.updateCurrency(dto.toCommand());

        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 한 건의 재화 정보 조회
     * @param currencyId
     * @return
     */
    @GetMapping("/{currencyId}")
    @Operation(summary = "한 건의 재화 정보 조회")
    public ResponseEntity<ApiResponse<CurrencyResponseDto>> selectOneCurrency(@PathVariable("currencyId") int currencyId){
        Result<CurrencyResponseDto> result = currencyService.selectOneCurrency(currencyId);

        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 전체 재화 정보 조회 (페이징 필요)
     * @return
     */
    @GetMapping
    @Operation(summary = "전체 재화 정보 조회")
    public  ResponseEntity<ApiResponse<List<CurrencyResponseDto>>> selectAllCurrencies(){
        return null;
    }
}
