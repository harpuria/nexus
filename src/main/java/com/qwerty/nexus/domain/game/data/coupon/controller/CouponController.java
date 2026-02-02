package com.qwerty.nexus.domain.game.data.coupon.controller;

import com.qwerty.nexus.domain.game.data.coupon.dto.request.CouponCreateRequestDto;
import com.qwerty.nexus.domain.game.data.coupon.dto.request.CouponUpdateRequestDto;
import com.qwerty.nexus.domain.game.data.coupon.dto.request.UseCouponRequestDto;
import com.qwerty.nexus.domain.game.data.coupon.dto.response.CouponListResponseDto;
import com.qwerty.nexus.domain.game.data.coupon.dto.response.CouponResponseDto;
import com.qwerty.nexus.domain.game.data.coupon.service.CouponService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.dto.PagingRequestDto;
import com.qwerty.nexus.global.response.ApiResponse;
import com.qwerty.nexus.global.response.ResponseEntityUtils;
import com.qwerty.nexus.global.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping(ApiConstants.Path.COUPON_PATH)
@RequiredArgsConstructor
@Tag(name = "쿠폰", description = "쿠폰 관련 API")
public class CouponController {
    private final CouponService couponService;

    @PostMapping
    @Operation(summary = "쿠폰 생성")
    public ResponseEntity<ApiResponse<Void>> createCoupon(@Valid @RequestBody CouponCreateRequestDto requestDto) {
        Result<Void> result = couponService.createCoupon(requestDto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.CREATED);
    }

    @PatchMapping("/{couponId}")
    @Operation(summary = "쿠폰 수정")
    public ResponseEntity<ApiResponse<Void>> updateCoupon(
            @PathVariable("couponId") Integer couponId,
            @Valid @RequestBody CouponUpdateRequestDto requestDto
    ) {
        requestDto.setCouponId(couponId);
        Result<Void> result = couponService.updateCoupon(requestDto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    @DeleteMapping("/{couponId}")
    @Operation(summary = "쿠폰 삭제")
    public ResponseEntity<ApiResponse<Void>> deleteCoupon(@PathVariable("couponId") Integer couponId) {
        Result<Void> result = couponService.deleteCoupon(couponId);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    @PostMapping("/use")
    @Operation(summary = "쿠폰 사용")
    public ResponseEntity<ApiResponse<Void>> useCoupon(@Valid @RequestBody UseCouponRequestDto requestDto) {
        Result<Void> result = couponService.useCoupon(requestDto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    @GetMapping("/{couponId}")
    @Operation(summary = "쿠폰 단건 조회")
    public ResponseEntity<ApiResponse<CouponResponseDto>> getCoupon(@PathVariable("couponId") Integer couponId) {
        Result<CouponResponseDto> result = couponService.getCoupon(couponId);
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping("/list/{gameId}")
    @Operation(summary = "쿠폰 목록 조회")
    public ResponseEntity<ApiResponse<CouponListResponseDto>> listCoupons(
            @PathVariable("gameId") Integer gameId,
            @RequestParam(defaultValue = "" + ApiConstants.Pagination.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = "" + ApiConstants.Pagination.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = ApiConstants.Pagination.DEFAULT_SORT_DIRECTION) String direction,
            @RequestParam(required = false) String keyword
    ) {
        PagingRequestDto pagingRequestDto = new PagingRequestDto();
        pagingRequestDto.setPage(page);
        pagingRequestDto.setSize(size);
        pagingRequestDto.setSort(sort);
        pagingRequestDto.setDirection(direction);
        pagingRequestDto.setKeyword(keyword);

        Result<CouponListResponseDto> result = couponService.listCoupons(pagingRequestDto, gameId);
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }
}
