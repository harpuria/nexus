package com.qwerty.nexus.domain.game.coupon.controller.admin;

import com.qwerty.nexus.domain.game.coupon.dto.request.CouponCreateRequestDto;
import com.qwerty.nexus.domain.game.coupon.dto.request.CouponUpdateRequestDto;
import com.qwerty.nexus.domain.game.coupon.dto.request.UseCouponRequestDto;
import com.qwerty.nexus.domain.game.coupon.dto.response.CouponListResponseDto;
import com.qwerty.nexus.domain.game.coupon.dto.response.CouponResponseDto;
import com.qwerty.nexus.domain.game.coupon.service.CouponService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.PagingRequestDto;
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
@RequestMapping(ApiConstants.Path.ADMIN_COUPON_PATH)
@RequiredArgsConstructor
@Tag(name = "쿠폰 메타데이터 (관리자)", description = "쿠폰 메타데이터 관련 API (관리자)")
public class CouponAdminController {
    private final CouponService couponService;

    /**
     * 쿠폰 메타데이터 생성
     * @param requestDto
     * @return
     */
    @PostMapping
    @Operation(summary = "쿠폰 메타데이터 생성")
    public ResponseEntity<ApiResponse<Void>> createCoupon(@Valid @RequestBody CouponCreateRequestDto requestDto) {
        Result<Void> result = couponService.createCoupon(requestDto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.CREATED);
    }

    /**
     * 쿠폰 메타데이터 수정
     * @param couponId
     * @param requestDto
     * @return
     */
    @PatchMapping("/{couponId}")
    @Operation(summary = "쿠폰 메타데이터 수정")
    public ResponseEntity<ApiResponse<Void>> updateCoupon(
            @PathVariable("couponId") Integer couponId,
            @Valid @RequestBody CouponUpdateRequestDto requestDto
    ) {
        requestDto.setCouponId(couponId);
        Result<Void> result = couponService.updateCoupon(requestDto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 쿠폰 삭제
     * @param couponId
     * @return
     */
    @DeleteMapping("/{couponId}")
    @Operation(summary = "쿠폰 메타데이터 삭제")
    public ResponseEntity<ApiResponse<Void>> deleteCoupon(@PathVariable("couponId") Integer couponId) {
        Result<Void> result = couponService.deleteCoupon(couponId);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 쿠폰 단건 조회
     * @param couponId
     * @return
     */
    @GetMapping("/{couponId}")
    @Operation(summary = "쿠폰 메타데이터 단건 조회")
    public ResponseEntity<ApiResponse<CouponResponseDto>> getCoupon(@PathVariable("couponId") Integer couponId) {
        Result<CouponResponseDto> result = couponService.getCoupon(couponId);
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 쿠폰 목록 조회
     * @param gameId
     * @param page
     * @param size
     * @param sort
     * @param direction
     * @param keyword
     * @return
     */
    @GetMapping("/list/{gameId}")
    @Operation(summary = "쿠폰 메타데이터 목록 조회")
    public ResponseEntity<ApiResponse<CouponListResponseDto>> listCoupons(
            @PathVariable("gameId") Integer gameId,
            @RequestParam(defaultValue = "" + ApiConstants.Pagination.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = "" + ApiConstants.Pagination.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = ApiConstants.Pagination.DEFAULT_SORT_DIRECTION) String direction,
            @RequestParam(required = false) String keyword
    ) {
        Result<CouponListResponseDto> result = couponService.listCoupons(
                buildPagingRequest(page, size, sort, direction, keyword),
                gameId
        );
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

    private PagingRequestDto buildPagingRequest(int page, int size, String sort, String direction, String keyword) {
        PagingRequestDto pagingRequestDto = new PagingRequestDto();
        pagingRequestDto.setPage(page);
        pagingRequestDto.setSize(size);
        pagingRequestDto.setSort(sort);
        pagingRequestDto.setDirection(direction);
        pagingRequestDto.setKeyword(keyword);
        return pagingRequestDto;
    }

    // TODO - 아래 API 들은 어드민에서 관리 및 테스트용도로 사용. 불필요시 추후 삭제하거나 코드 보완.

    /**
     * 쿠폰 사용
     * @param requestDto
     * @return
     */
    @PostMapping("/use")
    @Operation(summary = "쿠폰 사용")
    public ResponseEntity<ApiResponse<Void>> useCoupon(@Valid @RequestBody UseCouponRequestDto requestDto) {
        Result<Void> result = couponService.useCoupon(requestDto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }


}
