package com.qwerty.nexus.domain.game.data.product.controller;

import com.qwerty.nexus.domain.game.data.product.service.ProductService;
import com.qwerty.nexus.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/")
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

    @PostMapping("/1")
    @Operation(summary = "상품 정보 생성")
    public ResponseEntity<ApiResponse<Void>> create(){
        return null;
    }

    @PatchMapping("/2")
    @Operation(summary = "상품 정보 수정")
    public ResponseEntity<ApiResponse<Void>> update(){
        return null;
    }

    @DeleteMapping("/3")
    @Operation(summary = "상품 정보 삭제")
    public ResponseEntity<ApiResponse<Void>> delete(){
        return null;
    }

    @PostMapping("/4")
    @Operation(summary = "상품 구매 및 지급")
    public ResponseEntity<ApiResponse<Void>> buy(){
        return null;
    }
}
