package com.qwerty.nexus.domain.game.store.controller.client;

import com.qwerty.nexus.domain.game.store.dto.request.ShopProductPurchaseRequestDto;
import com.qwerty.nexus.domain.game.store.dto.response.ProductListResponseDto;
import com.qwerty.nexus.domain.game.store.dto.response.ShopListResponseDto;
import com.qwerty.nexus.domain.game.store.dto.response.ShopResponseDto;
import com.qwerty.nexus.domain.game.store.service.ProductService;
import com.qwerty.nexus.domain.game.store.service.ShopService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.PagingRequestDto;
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

@RestController
@RequestMapping(ApiConstants.Path.CLIENT_STORE_PATH)
@RequiredArgsConstructor
@Tag(name = "상점 (클라이언트)", description = "상점 조회 및 구매 API (클라이언트)")
public class StoreClientController {
    private final ProductService productService;
    private final ShopService shopService;

    @GetMapping("/shops")
    @Operation(summary = "상점 목록 조회")
    public ResponseEntity<ApiResponse<ShopListResponseDto>> listStoreShops(
            @RequestParam int gameId,
            @RequestParam(defaultValue = "" + ApiConstants.Pagination.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = "" + ApiConstants.Pagination.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = "sortOrder") String sort,
            @RequestParam(defaultValue = ApiConstants.Pagination.SORT_ASC) String direction
    ) {
        PagingRequestDto pagingRequestDto = new PagingRequestDto();
        pagingRequestDto.setPage(page);
        pagingRequestDto.setSize(size);
        pagingRequestDto.setSort(sort);
        pagingRequestDto.setDirection(direction);

        Result<ShopListResponseDto> result = shopService.listStoreShops(pagingRequestDto, gameId);
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping("/shops/{shopCode}")
    @Operation(summary = "상점 단건 조회")
    public ResponseEntity<ApiResponse<ShopResponseDto>> getStoreShop(
            @PathVariable String shopCode,
            @RequestParam int gameId
    ) {
        Result<ShopResponseDto> result = shopService.getStoreShop(gameId, shopCode);
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping("/shops/{shopCode}/products")
    @Operation(summary = "상점 상품 목록 조회")
    public ResponseEntity<ApiResponse<ProductListResponseDto>> listStoreProducts(
            @PathVariable String shopCode,
            @RequestParam int gameId,
            @RequestParam(defaultValue = "" + ApiConstants.Pagination.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = "" + ApiConstants.Pagination.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = "sortOrder") String sort,
            @RequestParam(defaultValue = ApiConstants.Pagination.SORT_ASC) String direction
    ) {
        PagingRequestDto pagingRequestDto = new PagingRequestDto();
        pagingRequestDto.setPage(page);
        pagingRequestDto.setSize(size);
        pagingRequestDto.setSort(sort);
        pagingRequestDto.setDirection(direction);

        Result<ProductListResponseDto> result = productService.listStoreProducts(pagingRequestDto, gameId, shopCode);
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

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
