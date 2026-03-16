package com.qwerty.nexus.domain.game.store.controller.admin;

import com.qwerty.nexus.domain.game.store.dto.request.ShopProductPurchaseRequestDto;
import com.qwerty.nexus.domain.game.store.dto.request.StoreProductCreateRequestDto;
import com.qwerty.nexus.domain.game.store.dto.request.StoreProductUpdateRequestDto;
import com.qwerty.nexus.domain.game.store.dto.response.StoreProductListResponseDto;
import com.qwerty.nexus.domain.game.store.dto.response.StoreProductResponseDto;
import com.qwerty.nexus.domain.game.store.service.ProductService;
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
@Tag(name = "상점 판매 상품 메타데이터 (관리자)", description = "상점 판매 상품 메타데이터 관련 API (관리자)")
public class StoreAdminController {
    private final StoreService storeService;
    private final ProductService productService;

    /**
     * 상점 판매 상품 등록
     * @param requestDto
     * @return
     */
    @PostMapping("/shop-products")
    @Operation(summary = "상점 판매 상품 등록")
    public ResponseEntity<ApiResponse<Void>> createShopProduct(@Valid @RequestBody StoreProductCreateRequestDto requestDto) {
        Result<Void> result = storeService.createShopProduct(requestDto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.CREATED);
    }

    /**
     * 상점 판매 상품 수정
     * @param shopProductId
     * @param requestDto
     * @return
     */
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

    /**
     * 상점 판매 상품 삭제
     * @param shopProductId
     * @param updatedBy
     * @return
     */
    @DeleteMapping("/shop-products/{shopProductId}")
    @Operation(summary = "상점 판매 상품 삭제")
    public ResponseEntity<ApiResponse<Void>> deleteShopProduct(
            @PathVariable("shopProductId") Integer shopProductId,
            @RequestParam @NotBlank(message = "updatedBy는 필수입니다.") @Parameter(example = "admin") String updatedBy
    ) {
        Result<Void> result = storeService.deleteShopProduct(shopProductId, updatedBy);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 상점 판매 상품 단건 조회
     * @param shopProductId
     * @return
     */
    @GetMapping("/shop-products/{shopProductId}")
    @Operation(summary = "상점 판매 상품 단건 조회")
    public ResponseEntity<ApiResponse<StoreProductResponseDto>> getShopProduct(
            @PathVariable("shopProductId") Integer shopProductId
    ) {
        Result<StoreProductResponseDto> result = storeService.getShopProduct(shopProductId);
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 상점 판매 상품 목록 조회
     * @param shopId
     * @param gameId
     * @param page
     * @param size
     * @param sort
     * @param direction
     * @return
     */
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

    /**
     * 상품 구매 (테스트용)
     * @param shopProductId
     * @param gameId
     * @param requestDto
     * @return
     */
    @PostMapping("/shop-products/{shopProductId}/purchase")
    @Operation(summary = "상품 구매")
    public ResponseEntity<ApiResponse<Void>> createStorePurchase(
            @PathVariable Integer shopProductId,
            @RequestParam int gameId,
            @Valid @RequestBody ShopProductPurchaseRequestDto requestDto
    ) {
        requestDto.setShopProductId(shopProductId);

        Result<Void> result = productService.purchaseStoreProduct(requestDto, gameId);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }
}
