package com.qwerty.nexus.domain.game.data.currency.controller;

import com.qwerty.nexus.domain.game.data.currency.command.CurrencyCreateCommand;
import com.qwerty.nexus.domain.game.data.currency.command.CurrencyUpdateCommand;
import com.qwerty.nexus.domain.game.data.currency.dto.request.CurrencyCreateRequestDto;
import com.qwerty.nexus.domain.game.data.currency.dto.request.CurrencyUpdateRequestDto;
import com.qwerty.nexus.domain.game.data.currency.dto.response.CurrencyResponseDto;
import com.qwerty.nexus.domain.game.data.currency.service.CurrencyService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.command.PagingCommand;
import com.qwerty.nexus.global.paging.dto.PagingRequestDto;
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
    private final CurrencyService service;

    /***
     * 재화 정보 생성
     * @param dto 생성할 재화 정보가 들어있는 객체 (DTO)
     * @return
     */
    @PostMapping
    @Operation(summary = "재화 정보 생성")
    public ResponseEntity<ApiResponse<Void>> createCurrency(@RequestBody CurrencyCreateRequestDto dto){
        Result<Void> result = service.create(CurrencyCreateCommand.from(dto));

        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.CREATED);
    }

    /**
     * 재화 정보 수정
     * @param dto 수정할 재화 정보가 들어있는 객체 (DTO)
     * @return
     */
    @PatchMapping("/{currencyId}")
    @Operation(summary = "재화 정보 수정")
    public ResponseEntity<ApiResponse<Void>> updateCurrency(@PathVariable("currencyId") int currencyId, @RequestBody CurrencyUpdateRequestDto dto){
        dto.setCurrencyId(currencyId);

        Result<Void> result = service.updateCurrency(CurrencyUpdateCommand.from(dto));

        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 재화 정보 삭제
     * @param currencyId 삭제할 재화의 아이디 (PK)
     * @return
     */
    @DeleteMapping("/{currencyId}")
    @Operation(summary = "재화 정보 삭제")
    public ResponseEntity<ApiResponse<Void>> deleteCurrency(@PathVariable("currencyId") int currencyId){
        CurrencyUpdateRequestDto dto = new CurrencyUpdateRequestDto();
        dto.setCurrencyId(currencyId);
        dto.setIsDel("Y");

        Result<Void> result = service.updateCurrency(CurrencyUpdateCommand.from(dto));

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
        Result<CurrencyResponseDto> result = service.selectOneCurrency(currencyId);

        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 전체 재화 정보 조회 (페이징 및 필터 지원)
     *
     * @param gameId         필터링할 게임 아이디
     * @param includeDeleted 삭제된 재화 포함 여부
     * @param page           페이지 번호 (옵션)
     * @param size           페이지 사이즈 (옵션)
     * @param sort           정렬 컬럼 (옵션)
     * @param direction      정렬 방향 (옵션)
     * @param keyword        검색 키워드 (옵션)
     * @return 재화 목록
     */
    @GetMapping
    @Operation(summary = "전체 재화 정보 조회", description = "게임 ID, 삭제 여부, 페이징 파라미터를 통해 재화 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<CurrencyResponseDto>>> selectAllCurrencies(
            @RequestParam(value = "gameId", required = false) Integer gameId,
            @RequestParam(value = "includeDeleted", required = false, defaultValue = "false") boolean includeDeleted,
            @RequestParam(value = ApiConstants.Pagination.PAGE_PARAM, required = false) Integer page,
            @RequestParam(value = ApiConstants.Pagination.SIZE_PARAM, required = false) Integer size,
            @RequestParam(value = ApiConstants.Pagination.SORT_PARAM, required = false) String sort,
            @RequestParam(value = "direction", required = false) String direction,
            @RequestParam(value = "keyword", required = false) String keyword
    ){
        PagingCommand pagingCommand = null;
        boolean pagingRequested = page != null || size != null || sort != null || direction != null || keyword != null;

        if(pagingRequested){
            PagingRequestDto pagingRequestDto = new PagingRequestDto();
            pagingRequestDto.setPage(page != null ? page : ApiConstants.Pagination.DEFAULT_PAGE_NUMBER);
            pagingRequestDto.setSize(size != null ? size : ApiConstants.Pagination.DEFAULT_PAGE_SIZE);
            if(sort != null){
                pagingRequestDto.setSort(sort);
            }
            if(direction != null){
                pagingRequestDto.setDirection(direction);
            }
            if(keyword != null){
                pagingRequestDto.setKeyword(keyword);
            }

            pagingCommand = PagingCommand.from(pagingRequestDto);
        }

        Result<List<CurrencyResponseDto>> result = service.listCurrencies(pagingCommand, gameId, includeDeleted);

        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }
}
