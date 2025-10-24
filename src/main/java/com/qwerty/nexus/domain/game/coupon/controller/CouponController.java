package com.qwerty.nexus.domain.game.coupon.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qwerty.nexus.domain.game.coupon.command.CouponCreateCommand;
import com.qwerty.nexus.domain.game.coupon.command.CouponGrantCommand;
import com.qwerty.nexus.domain.game.coupon.command.CouponUpdateCommand;
import com.qwerty.nexus.domain.game.coupon.dto.request.CouponCreateRequestDto;
import com.qwerty.nexus.domain.game.coupon.dto.request.CouponGrantRequestDto;
import com.qwerty.nexus.domain.game.coupon.dto.request.CouponUpdateRequestDto;
import com.qwerty.nexus.domain.game.coupon.service.CouponService;
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

@Log4j2
@RestController
@RequestMapping(ApiConstants.Path.COUPON_PATH)
@RequiredArgsConstructor
@Tag(name = "쿠폰", description = "쿠폰 관련 API")
public class CouponController {
    private final CouponService couponService;

    @PostMapping
    @Operation(summary = "쿠폰 생성")
    public ResponseEntity<ApiResponse<Void>> create(@RequestBody CouponCreateRequestDto dto) {
        Result<Void> result = couponService.create(CouponCreateCommand.from(dto));
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.CREATED);
    }

    @PatchMapping("/{couponId}")
    @Operation(summary = "쿠폰 수정")
    public ResponseEntity<ApiResponse<Void>> update(@PathVariable int couponId, @RequestBody CouponUpdateRequestDto dto) {
        dto.setCouponId(couponId);
        Result<Void> result = couponService.update(CouponUpdateCommand.from(dto));
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    @PostMapping("/{code}/grant")
    @Operation(summary = "쿠폰 지급")
    public ResponseEntity<ApiResponse<Void>> grant(@PathVariable String code, @RequestBody CouponGrantRequestDto dto) throws JsonProcessingException {
        Result<Void> result = couponService.grant(CouponGrantCommand.from(code, dto));
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }
}
