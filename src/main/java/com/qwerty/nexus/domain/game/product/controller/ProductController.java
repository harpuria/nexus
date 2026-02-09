package com.qwerty.nexus.domain.game.product.controller;

import com.qwerty.nexus.domain.game.product.dto.request.ProductBuyRequestDto;
import com.qwerty.nexus.domain.game.product.dto.request.ProductCreateRequestDto;
import com.qwerty.nexus.domain.game.product.dto.request.ProductUpdateRequestDto;
import com.qwerty.nexus.domain.game.product.dto.response.ProductDetailResponseDto;
import com.qwerty.nexus.domain.game.product.dto.response.ProductListResponseDto;
import com.qwerty.nexus.domain.game.product.service.ProductService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.dto.PagingRequestDto;
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
@RequestMapping(ApiConstants.Path.PRODUCT_PATH)
@RequiredArgsConstructor
@Tag(name = "상품", description = "상품 관련 API")
public class ProductController {
    private final ProductService productService;

    /**
     * 상품 정보 생성
     * @param requestDto
     * @return
     */
    @PostMapping
    @Operation(summary = "상품 정보 생성")
    public ResponseEntity<ApiResponse<Void>> createProduct(@Valid @RequestBody ProductCreateRequestDto requestDto){
        Result<Void> result = productService.createProduct(requestDto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.CREATED);
    }

    /**
     * 상품 정보 수정
     * @param requestDto
     * @return
     */
    @PatchMapping("/{productId}")
    @Operation(summary = "상품 정보 수정")
    public ResponseEntity<ApiResponse<Void>> updateProduct(
            @PathVariable("productId") Integer productId,
            @Valid @RequestBody ProductUpdateRequestDto requestDto
    ){
        requestDto.setProductId(productId);

        Result<Void> result = productService.updateProduct(requestDto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 상품 정보 삭제 (논리적 삭제)
     * @param productId
     * @return
     */
    @DeleteMapping("/{productId}")
    @Operation(summary = "상품 정보 삭제")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable("productId") Integer productId){
        Result<Void> result = productService.deleteProduct(productId);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 상품 구매 및 지급
     * @param requestDto
     * @return
     */
    @PostMapping("/buy")
    @Operation(summary = "상품 구매 및 지급")
    public ResponseEntity<ApiResponse<Void>> buyProduct(@Valid @RequestBody ProductBuyRequestDto requestDto) {
        Result<Void> result = productService.buyProduct(requestDto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 상품 목록 조회
     * @param gameId
     * @param page
     * @param size
     * @param keyword
     * @return
     */
    @GetMapping("/list/{gameId}")
    @Operation(summary = "상품 목록 조회")
    public ResponseEntity<ApiResponse<ProductListResponseDto>> listProducts(
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

        Result<ProductListResponseDto> result = productService.listProducts(pagingRequestDto, gameId);

        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 하나의 상품 조회
     * @param productId
     * @return
     */
    @GetMapping("/{productId}")
    @Operation(summary = "하나의 상품 조회")
    public ResponseEntity<ApiResponse<ProductDetailResponseDto>> getProduct(@PathVariable("productId") int productId){
        Result<ProductDetailResponseDto> result = productService.getProduct(productId);
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }
}
