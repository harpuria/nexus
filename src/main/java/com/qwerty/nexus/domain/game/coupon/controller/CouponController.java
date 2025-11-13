package com.qwerty.nexus.domain.game.coupon.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qwerty.nexus.domain.game.coupon.command.CouponCreateCommand;
import com.qwerty.nexus.domain.game.coupon.command.CouponGrantCommand;
import com.qwerty.nexus.domain.game.coupon.command.CouponSearchCommand;
import com.qwerty.nexus.domain.game.coupon.command.CouponUpdateCommand;
import com.qwerty.nexus.domain.game.coupon.dto.request.CouponCreateRequestDto;
import com.qwerty.nexus.domain.game.coupon.dto.request.CouponGrantRequestDto;
import com.qwerty.nexus.domain.game.coupon.dto.request.CouponUpdateRequestDto;
import com.qwerty.nexus.domain.game.coupon.dto.response.CouponListResponseDto;
import com.qwerty.nexus.domain.game.coupon.service.CouponService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.response.ApiResponse;
import com.qwerty.nexus.global.response.ResponseEntityUtils;
import com.qwerty.nexus.global.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import java.time.OffsetDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

/**
 * TODO : 쿠폰 관련 작업
 * 쿠폰 관련된 작업은 Codex 에서 생성한 상태임.
 * 다른 작업들 한 뒤에 추후 세세한 부분 수정할 필요 있음.
 */

@Log4j2
@RestController
@RequestMapping(ApiConstants.Path.COUPON_PATH)
@RequiredArgsConstructor
@Tag(name = "쿠폰", description = "쿠폰 관련 API")
public class CouponController {
    private final CouponService couponService;

    @GetMapping
    @Operation(summary = "쿠폰 목록 조회")
    public ResponseEntity<ApiResponse<CouponListResponseDto>> listCoupons(
            @RequestParam Integer gameId,
            @RequestParam(defaultValue = "" + ApiConstants.Pagination.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = "" + ApiConstants.Pagination.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String direction,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String isDel,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateTo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime createdFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime createdTo
    ) {
        CouponSearchCommand command = CouponSearchCommand.builder()
                .gameId(gameId)
                .page(page)
                .size(size)
                .sort(sort)
                .direction(direction)
                .code(code)
                .keyword(keyword)
                .isDel(isDel)
                .startDateFrom(startDateFrom)
                .startDateTo(startDateTo)
                .endDateFrom(endDateFrom)
                .endDateTo(endDateTo)
                .createdFrom(createdFrom)
                .createdTo(createdTo)
                .build();

        Result<CouponListResponseDto> result = couponService.listCoupons(command);
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 쿠폰 생성
     * @param dto
     * @return
     */
    @PostMapping
    @Operation(summary = "쿠폰 생성")
    public ResponseEntity<ApiResponse<Void>> create(@RequestBody CouponCreateRequestDto dto) {
        Result<Void> result = couponService.create(CouponCreateCommand.from(dto));
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.CREATED);
    }

    /**
     * 쿠폰 수정
     * @param couponId
     * @param dto
     * @return
     */
    @PatchMapping("/{couponId}")
    @Operation(summary = "쿠폰 수정")
    public ResponseEntity<ApiResponse<Void>> update(@PathVariable int couponId, @RequestBody CouponUpdateRequestDto dto) {
        dto.setCouponId(couponId);
        Result<Void> result = couponService.update(CouponUpdateCommand.from(dto));
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 쿠폰 지급
     * @param dto
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/grant")
    @Operation(summary = "쿠폰 지급")
    public ResponseEntity<ApiResponse<Void>> grant(@RequestBody CouponGrantRequestDto dto) throws JsonProcessingException {
        Result<Void> result = couponService.grant(CouponGrantCommand.from(dto));
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }
}
