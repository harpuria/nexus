package com.qwerty.nexus.domain.game.coupon.controller.client;

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
@RequestMapping(ApiConstants.Path.CLIENT_COUPON_PATH)
@RequiredArgsConstructor
@Tag(name = "쿠폰 (클라이언트)", description = "쿠폰 관련 API (클라이언트)")
public class CouponClientController {
    private final CouponService couponService;

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
