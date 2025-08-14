package com.qwerty.nexus.domain.gameData.currency.controller;

import com.qwerty.nexus.domain.gameData.currency.dto.request.CurrencyCreateRequestDto;
import com.qwerty.nexus.domain.gameData.currency.dto.request.CurrencyUpdateRequestDto;
import com.qwerty.nexus.domain.gameData.currency.dto.response.CurrencyResponseDto;
import com.qwerty.nexus.domain.gameData.currency.service.CurrencyService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
    public ResponseEntity<ApiResponse<Void>> createCurrency(@RequestBody CurrencyCreateRequestDto dto){
        return null;
    }

    /**
     * 재화 정보 수정
     * @param dto
     * @return
     */
    @PatchMapping("/{currencyId}")
    public ResponseEntity<ApiResponse<Void>> updateCurrency(@PathVariable("currencyId") String currencyId, @RequestBody CurrencyUpdateRequestDto dto){
        return null;
    }

    /**
     * 재화 정보 삭제
     * @param currencyId
     * @return
     */
    @DeleteMapping("/{currencyId}")
    public ResponseEntity<ApiResponse<Void>> deleteCurrency(@PathVariable("currencyId") String currencyId){
        return null;
    }

    /**
     * 한 건의 재화 정보 조회
     * @param currencyId
     * @return
     */
    @GetMapping("/{currencyId}")
    public ResponseEntity<ApiResponse<CurrencyResponseDto>> selectOneCurrency(@PathVariable("currencyId") String currencyId){
        return null;
    }

    /**
     * 전체 재화 정보 조회 (페이징 필요)
     * @return
     */
    @GetMapping
    public  ResponseEntity<ApiResponse<List<CurrencyResponseDto>>> selectAllCurrencies(){
        return null;
    }
}
