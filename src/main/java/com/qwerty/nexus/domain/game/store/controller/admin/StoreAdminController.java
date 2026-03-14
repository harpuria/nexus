package com.qwerty.nexus.domain.game.store.controller.admin;

import com.qwerty.nexus.domain.game.store.dto.request.StoreProductCreateRequestDto;
import com.qwerty.nexus.domain.game.store.dto.request.StoreProductUpdateRequestDto;
import com.qwerty.nexus.domain.game.store.dto.response.StoreProductListResponseDto;
import com.qwerty.nexus.domain.game.store.dto.response.StoreProductResponseDto;
import com.qwerty.nexus.domain.game.store.service.StoreService;
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
@RequestMapping(ApiConstants.Path.ADMIN_STORE_PATH)
@RequiredArgsConstructor
@Validated
@Tag(name = "스토어 (관리자)", description = "스토어 상점 판매 상품 구성 API (관리자)")
public class StoreAdminController {
    private final StoreService storeService;

    @PostMapping("/shop-products")
    @Operation(summary = "상점 판매 상품 등록")
    public ResponseEntity<ApiResponse<Void>> createShopProduct(@Valid @RequestBody StoreProductCreateRequestDto requestDto) {
        Result<Void> result = storeService.createShopProduct(requestDto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.CREATED);
    }

    @PatchMapping("/shop-products/{shopProductId}")
    @Operation(summary = "상점 판매 상품 수정")
    public ResponseEntity<ApiResponse<Void>> updateShopProduct(
            @PathVariable("shopProductId") Integer shopProductId,
            @Valid @RequestBody StoreProductUpdateRequestDto requestDto
    ) {
        requestDto.setShopProductId(shopProductId);

        Result<Void> result = storeService.updateShopProduct(requestDto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    @DeleteMapping("/shop-products/{shopProductId}")
    @Operation(summary = "상점 판매 상품 삭제")
    public ResponseEntity<ApiResponse<Void>> deleteShopProduct(
            @PathVariable("shopProductId") Integer shopProductId,
            @RequestParam @NotBlank(message = "updatedBy는 필수입니다.") @Parameter(example = "admin") String updatedBy
    ) {
        Result<Void> result = storeService.deleteShopProduct(shopProductId, updatedBy);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    @GetMapping("/shop-products/{shopProductId}")
    @Operation(summary = "상점 판매 상품 단건 조회")
    public ResponseEntity<ApiResponse<StoreProductResponseDto>> getShopProduct(
            @PathVariable("shopProductId") Integer shopProductId
    ) {
        Result<StoreProductResponseDto> result = storeService.getShopProduct(shopProductId);
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping("/shops/{shopId}/shop-products")
    @Operation(summary = "상점 판매 상품 목록 조회")
    public ResponseEntity<ApiResponse<StoreProductListResponseDto>> listShopProducts(
            @PathVariable("shopId") int shopId,
            @RequestParam int gameId,
            @RequestParam(defaultValue = "" + ApiConstants.Pagination.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = "" + ApiConstants.Pagination.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = ApiConstants.Pagination.DEFAULT_SORT_DIRECTION) String direction
    ) {
        PagingRequestDto pagingRequestDto = new PagingRequestDto();
        pagingRequestDto.setPage(page);
        pagingRequestDto.setSize(size);
        pagingRequestDto.setSort(sort);
        pagingRequestDto.setDirection(direction);

        Result<StoreProductListResponseDto> result = storeService.listShopProducts(pagingRequestDto, gameId, shopId);
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }
}
