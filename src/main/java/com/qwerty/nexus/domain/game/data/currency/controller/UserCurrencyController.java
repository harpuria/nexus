package com.qwerty.nexus.domain.game.data.currency.controller;

import com.qwerty.nexus.domain.game.data.currency.dto.request.UserCurrencyCreateRequestDto;
import com.qwerty.nexus.domain.game.data.currency.dto.request.UserCurrencyOperateRequestDto;
import com.qwerty.nexus.domain.game.data.currency.dto.request.UserCurrencyUpdateRequestDto;
import com.qwerty.nexus.domain.game.data.currency.dto.response.UserCurrencyListResponseDto;
import com.qwerty.nexus.domain.game.data.currency.dto.response.UserCurrencyResponseDto;
import com.qwerty.nexus.domain.game.data.currency.service.UserCurrencyService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.dto.PagingRequestDto;
import com.qwerty.nexus.global.response.ApiResponse;
import com.qwerty.nexus.global.response.ResponseEntityUtils;
import com.qwerty.nexus.global.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(ApiConstants.Path.USER_CURRENCY_PATH)
@RestController
@RequiredArgsConstructor
@Tag(name = "유저 재화", description = "유저 재화 관련 API")
public class UserCurrencyController {
    private final UserCurrencyService service;

    /**
     * 유저 재화 생성
     * @param dto
     * @return
     */
    @PostMapping
    @Operation(summary = "유저 재화 생성")
    public ResponseEntity<ApiResponse<Void>> createUserCurrency(@Valid @RequestBody UserCurrencyCreateRequestDto dto){
        Result<Void> result = service.createUserCurrency(dto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.CREATED);
    }

    /**
     * 유저 재화 수정
     * @param dto
     * @return
     */
    @PatchMapping("/{userCurrencyId}")
    @Operation(summary = "유저 재화 수정")
    public ResponseEntity<ApiResponse<Void>> updateUserCurrency(
            @PathVariable("userCurrencyId") int userCurrencyId,
            @Valid @RequestBody UserCurrencyUpdateRequestDto dto
    ){
        dto.setUserCurrencyId(userCurrencyId);

        Result<Void> result = service.updateUserCurrency(dto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 유저 재화 삭제 (논리적 삭제)
     * @param userCurrencyId
     * @return
     */
    @DeleteMapping("/{userCurrencyId}")
    @Operation(summary = "유저 재화 삭제")
    public ResponseEntity<ApiResponse<Void>> deleteUserCurrency(@PathVariable("userCurrencyId") int userCurrencyId){
        UserCurrencyUpdateRequestDto dto = new UserCurrencyUpdateRequestDto();
        dto.setUserCurrencyId(userCurrencyId);
        dto.setIsDel("Y");

        Result<Void> result = service.updateUserCurrency(dto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 유저 재화 연산
     * @param userCurrencyId
     * @param dto
     * @return
     */
    @PatchMapping("/operation/{userCurrencyId}")
    @Operation(summary = "유저 재화 연산")
    public ResponseEntity<ApiResponse<UserCurrencyResponseDto>> operateUserCurrency(
            @PathVariable("userCurrencyId") int userCurrencyId,
            @Valid @RequestBody UserCurrencyOperateRequestDto dto
    ){
        dto.setUserCurrencyId(userCurrencyId);

        Result<UserCurrencyResponseDto> result = service.operateUserCurrency(dto);
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping("/list/{userId}")
    @Operation(summary = "유저 재화 목록 조회")
    public ResponseEntity<ApiResponse<UserCurrencyListResponseDto>> listUserCurrencies(
            @PathVariable("userId") int userId,
            @RequestParam(defaultValue = "" + ApiConstants.Pagination.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = "" + ApiConstants.Pagination.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = ApiConstants.Pagination.DEFAULT_SORT_FIELD) String sort,
            @RequestParam(defaultValue = ApiConstants.Pagination.DEFAULT_SORT_DIRECTION) String direction,
            @RequestParam(required = false) Integer currencyId,
            @RequestParam(required = false) Integer gameId
    ) {
        PagingRequestDto pagingRequestDto = new PagingRequestDto();
        pagingRequestDto.setPage(page);
        pagingRequestDto.setSize(size);
        pagingRequestDto.setSort(sort);
        pagingRequestDto.setDirection(direction);

        Result<UserCurrencyListResponseDto> result = service.listUserCurrencies(
                pagingRequestDto,
                userId,
                gameId,
                currencyId
        );

        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

}
