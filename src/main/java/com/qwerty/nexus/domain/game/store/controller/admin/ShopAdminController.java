package com.qwerty.nexus.domain.game.store.controller.admin;

import com.qwerty.nexus.domain.game.store.dto.request.ShopCreateRequestDto;
import com.qwerty.nexus.domain.game.store.dto.request.ShopUpdateRequestDto;
import com.qwerty.nexus.domain.game.store.dto.response.ShopListResponseDto;
import com.qwerty.nexus.domain.game.store.dto.response.ShopResponseDto;
import com.qwerty.nexus.domain.game.store.service.ShopService;
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
@RequestMapping(ApiConstants.Path.ADMIN_SHOP_PATH)
@RequiredArgsConstructor
@Validated
@Tag(name = "상점 메타데이터 (관리자)", description = "상점 메타데이터 API (관리자)")
public class ShopAdminController {
    private final ShopService shopService;

    /**
     * 상점 메타데이터 생성
     * @param requestDto
     * @return
     */
    @PostMapping
    @Operation(summary = "상점 메타데이터 생성")
    public ResponseEntity<ApiResponse<Void>> createShop(@Valid @RequestBody ShopCreateRequestDto requestDto) {
        Result<Void> result = shopService.createShop(requestDto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.CREATED);
    }

    /**
     * 상점 메타데이터 수정
     * @param shopId
     * @param requestDto
     * @return
     */
    @PatchMapping("/{shopId}")
    @Operation(summary = "상점 메타데이터 수정")
    public ResponseEntity<ApiResponse<Void>> updateShop(
            @PathVariable("shopId") Integer shopId,
            @Valid @RequestBody ShopUpdateRequestDto requestDto
    ) {
        requestDto.setShopId(shopId);

        Result<Void> result = shopService.updateShop(requestDto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 상점 메타데이터 삭제
     * @param shopId
     * @param updatedBy
     * @return
     */
    @DeleteMapping("/{shopId}")
    @Operation(summary = "상점 메타데이터 삭제")
    public ResponseEntity<ApiResponse<Void>> deleteShop(
            @PathVariable("shopId") Integer shopId,
            @RequestParam @NotBlank(message = "updatedBy는 필수입니다.") @Parameter(example = "admin") String updatedBy
    ) {
        Result<Void> result = shopService.deleteShop(shopId, updatedBy);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 상점 단건 조회
     * @param shopId
     * @return
     */
    @GetMapping("/{shopId}")
    @Operation(summary = "상점 단건 조회")
    public ResponseEntity<ApiResponse<ShopResponseDto>> getShop(@PathVariable("shopId") Integer shopId) {
        Result<ShopResponseDto> result = shopService.getShop(shopId);
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 상점 목록 조회
     * @param gameId
     * @param page
     * @param size
     * @param sort
     * @param direction
     * @param keyword
     * @return
     */
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
