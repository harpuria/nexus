package com.qwerty.nexus.domain.game.product.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping(ApiConstants.Path.PRODUCT_PATH)
@RequiredArgsConstructor
@Tag(name = "상품", description = "상품 관련 API")
public class ProductController {
    private final ProductService service;

    /**
     * 상품 정보 생성
     * @param dto
     * @return
     */
    @PostMapping
    @Operation(summary = "상품 정보 생성")
    public ResponseEntity<ApiResponse<Void>> create(@RequestBody ProductCreateRequestDto dto){
        Result<Void> rst = service.create(dto);
        return ResponseEntityUtils.toResponseEntityVoid(rst, HttpStatus.CREATED);
    }

    /**
     * 상품 정보 수정
     * @param dto
     * @return
     */
    @PatchMapping("/{productId}")
    @Operation(summary = "상품 정보 수정")
    public ResponseEntity<ApiResponse<Void>> update(@PathVariable int productId, @RequestBody ProductUpdateRequestDto dto){
        dto.setProductId(productId);

        Result<Void> rst = service.update(dto);
        return ResponseEntityUtils.toResponseEntityVoid(rst, HttpStatus.OK);
    }

    /**
     * 상품 정보 삭제 (논리적 삭제)
     * @param productId
     * @return
     */
    @DeleteMapping("/{productId}")
    @Operation(summary = "상품 정보 삭제")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable int productId){
        ProductUpdateRequestDto dto = new ProductUpdateRequestDto();
        dto.setIsDel("Y");

        Result<Void> rst = service.update(dto);
        return ResponseEntityUtils.toResponseEntityVoid(rst, HttpStatus.OK);
    }

    /**
     * 상품 구매 및 지급
     * @param dto
     * @return
     */
    @PostMapping("/buy")
    @Operation(summary = "상품 구매 및 지급")
    public ResponseEntity<ApiResponse<Void>> buy(ProductBuyRequestDto dto) throws JsonProcessingException {
        Result<Void> rst = service.buy(dto);
        return ResponseEntityUtils.toResponseEntityVoid(rst, HttpStatus.OK);
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
    public ResponseEntity<ApiResponse<ProductListResponseDto>> list(
            @RequestParam int gameId,
            @RequestParam(defaultValue = "" + ApiConstants.Pagination.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = "" + ApiConstants.Pagination.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(required = false) String keyword
    ) {

        PagingRequestDto pagingRequestDto = new PagingRequestDto();
        pagingRequestDto.setPage(page);
        pagingRequestDto.setSize(size);
        pagingRequestDto.setKeyword(keyword);

        Result<ProductListResponseDto> rst = service.list(pagingRequestDto, gameId);

        return ResponseEntityUtils.toResponseEntity(rst, HttpStatus.OK);
    }

    /**
     * 하나의 상품 조회
     * @param productId
     * @return
     */
    @GetMapping("/{productId}")
    @Operation(summary = "하나의 상품 조회")
    public ResponseEntity<ApiResponse<ProductDetailResponseDto>> selectOneProduct(@PathVariable("productId") int productId){
        Result<ProductDetailResponseDto> rst = service.selectOneProduct(productId);
        return ResponseEntityUtils.toResponseEntity(rst, HttpStatus.OK);
    }
}
