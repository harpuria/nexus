package com.qwerty.nexus.domain.game.data.product.controller;

import com.qwerty.nexus.domain.game.data.product.command.ProductCreateCommand;
import com.qwerty.nexus.domain.game.data.product.command.ProductUpdateCommand;
import com.qwerty.nexus.domain.game.data.product.dto.request.ProductCreateRequestDto;
import com.qwerty.nexus.domain.game.data.product.dto.request.ProductUpdateRequestDto;
import com.qwerty.nexus.domain.game.data.product.dto.response.ProductResponseDto;
import com.qwerty.nexus.domain.game.data.product.service.ProductService;
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
@RequestMapping(ApiConstants.Path.PRODUCT_PATH)
@RequiredArgsConstructor
@Tag(name = "상품", description = "상품 관련 API")
public class ProductController {
    private final ProductService service;

    /*
     *  여기엔 어떤 API를 만들어야 할까..?
     *
     * 1) 상품 정보 생성
     * 2) 상품 정보 수정
     * 3) 상품 정보 삭제
     * 4) 상품 구매 및 지급
     *
     */

    /**
     * 상품 정보 생성
     * @param dto
     * @return
     */
    @PostMapping
    @Operation(summary = "상품 정보 생성")
    public ResponseEntity<ApiResponse<Void>> create(@RequestBody ProductCreateRequestDto dto){
        Result<Void> rst = service.create(ProductCreateCommand.from(dto));
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

        Result<Void> rst = service.update(ProductUpdateCommand.from(dto));

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

        Result<Void> rst = service.update(ProductUpdateCommand.from(dto));

        return ResponseEntityUtils.toResponseEntityVoid(rst, HttpStatus.OK);
    }

    @PostMapping("/4")
    @Operation(summary = "상품 구매 및 지급")
    public ResponseEntity<ApiResponse<Void>> buy(){
        return null;
    }
}
