package com.qwerty.nexus.domain.game.store.shop.controller;

import com.qwerty.nexus.domain.game.store.shop.dto.request.ShopCreateRequestDto;
import com.qwerty.nexus.domain.game.store.shop.dto.request.ShopUpdateRequestDto;
import com.qwerty.nexus.domain.game.store.shop.dto.response.ShopListResponseDto;
import com.qwerty.nexus.domain.game.store.shop.dto.response.ShopResponseDto;
import com.qwerty.nexus.domain.game.store.shop.service.ShopService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.PagingRequestDto;
import com.qwerty.nexus.global.response.ApiResponse;
import com.qwerty.nexus.global.response.ResponseEntityUtils;
import com.qwerty.nexus.global.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.Path.SHOP_PATH)
@RequiredArgsConstructor
@Validated
@Tag(name = "상점", description = "상점 관련 API")
public class ShopController {
    private final ShopService shopService;

    @PostMapping
    @Operation(summary = "상점 생성")
    public ResponseEntity<ApiResponse<Void>> createShop(@Valid @RequestBody ShopCreateRequestDto requestDto) {
        Result<Void> result = shopService.createShop(requestDto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.CREATED);
    }

    @PatchMapping("/{shopId}")
    @Operation(summary = "상점 수정")
    public ResponseEntity<ApiResponse<Void>> updateShop(
            @PathVariable("shopId") Integer shopId,
            @Valid @RequestBody ShopUpdateRequestDto requestDto
    ) {
        requestDto.setShopId(shopId);

        Result<Void> result = shopService.updateShop(requestDto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    @DeleteMapping("/{shopId}")
    @Operation(summary = "상점 삭제")
    public ResponseEntity<ApiResponse<Void>> deleteShop(
            @PathVariable("shopId") Integer shopId,
            @RequestParam @NotBlank(message = "updatedBy는 필수입니다.") @Parameter(example = "admin") String updatedBy
    ) {
        Result<Void> result = shopService.deleteShop(shopId, updatedBy);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    @GetMapping("/{shopId}")
    @Operation(summary = "상점 단건 조회")
    public ResponseEntity<ApiResponse<ShopResponseDto>> getShop(@PathVariable("shopId") Integer shopId) {
        Result<ShopResponseDto> result = shopService.getShop(shopId);
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping("/list/{gameId}")
    @Operation(summary = "상점 목록 조회")
    public ResponseEntity<ApiResponse<ShopListResponseDto>> listShops(
            @PathVariable("gameId") int gameId,
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

        Result<ShopListResponseDto> result = shopService.listShops(pagingRequestDto, gameId);
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }
}
